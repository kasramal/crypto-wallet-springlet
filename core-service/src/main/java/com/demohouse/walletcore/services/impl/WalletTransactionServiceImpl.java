package com.demohouse.walletcore.services.impl;

import com.demohouse.walletcore.core.fees.EstimatedFee;
import com.demohouse.walletcore.core.fees.FeeUtils;
import com.demohouse.walletcore.core.manager.CoinManagerFactory;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.entities.Wallet;
import com.demohouse.walletcore.entities.WalletTransaction;
import com.demohouse.walletcore.repositories.WalletRepository;
import com.demohouse.walletcore.repositories.WalletTransactionRepository;
import com.demohouse.walletcore.services.WalletTransactionService;
import com.demohouse.walletcore.services.dto.WalletTransactionDTO;
import com.demohouse.walletcore.services.dto.WalletTransactionInquiryDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final CoinManagerFactory coinManagerFactory;
    private final FeeUtils feeUtils;

    public WalletTransactionServiceImpl(WalletRepository walletRepository,
                                        WalletTransactionRepository walletTransactionRepository,
                                        CoinManagerFactory coinManagerFactory,
                                        FeeUtils feeUtils) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.coinManagerFactory = coinManagerFactory;
        this.feeUtils = feeUtils;
    }

    @Override
    public String signedTransaction(Coin coin, WalletTransactionDTO walletTransactionDTO) {
        Wallet wallet = this.fetchWallet(coin, walletTransactionDTO.getFrom(), walletTransactionDTO.getPrivateKey());
        EstimatedFee fee = feeUtils.getFee(coin);
        BigDecimal value = walletTransactionDTO.getValue() != null ?
                walletTransactionDTO.getValue() : this.calculateTransactionDisChargeValue(coin, walletTransactionDTO.getFrom(), fee.getInstantFee());
        return this.coinManagerFactory.getManager(coin).createTransactions(
                wallet.getPrivateKey(),
                walletTransactionDTO.getFrom(),
                walletTransactionDTO.getTo(),
                value,
                fee.getInstantFee()
        );
    }

    @Override
    public String pushRawTransaction(Coin coin, WalletTransactionDTO walletTransactionDTO) {
        Wallet wallet = this.fetchWallet(coin, walletTransactionDTO.getFrom(), walletTransactionDTO.getPrivateKey());
        EstimatedFee fee = feeUtils.getFee(coin);
        BigDecimal value = walletTransactionDTO.getValue() != null ?
                walletTransactionDTO.getValue() : this.calculateTransactionDisChargeValue(coin, walletTransactionDTO.getFrom(), fee.getInstantFee());
        TransactionResult transactionResult = this
                .coinManagerFactory.getManager(coin).pushTransaction(
                        wallet.getPrivateKey(),
                        walletTransactionDTO.getFrom(),
                        walletTransactionDTO.getTo(),
                        value,
                        fee.getInstantFee()
                );
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .from(wallet)
                .to(walletTransactionDTO.getTo())
                .fee(transactionResult.getFee())
                .value(value)
                .provider(transactionResult.getProvider())
                .transactionId(transactionResult.getPayload().getTxid())
                .coin(coin)
                .actionDate(ZonedDateTime.now())
                .build();
        walletTransaction = this.walletTransactionRepository.save(walletTransaction);
        return walletTransaction.getTransactionId();
    }

    @Override
    public String pushHexTransaction(Coin coin, String transactionHex) {
        TransactionResult transactionResult = this.coinManagerFactory.getManager(coin).pushTransaction(transactionHex);
        return transactionResult.getPayload().getTxid();
    }

    @Override
    public WalletTransactionInquiryDTO inquiryTransaction(Coin coin, String transactionId) {
        Optional<WalletTransaction> byCoinAndTransactionId = this.walletTransactionRepository.findByCoinAndTransactionId(coin, transactionId);
        WalletTransactionInquiryDTO walletTransactionInquiryDTO = new WalletTransactionInquiryDTO();
        walletTransactionInquiryDTO.setTransactionId(transactionId);
        if (byCoinAndTransactionId.isPresent()) {
            WalletTransaction walletTransaction = byCoinAndTransactionId.get();
            walletTransactionInquiryDTO.setFrom(walletTransaction.getFrom().getAddress());
            walletTransactionInquiryDTO.setTo(walletTransaction.getTo());
            walletTransactionInquiryDTO.setFee(walletTransaction.getFee());
            walletTransactionInquiryDTO.setValue(walletTransaction.getValue());
            if (walletTransaction.isConfirmed()) {
                walletTransactionInquiryDTO.setConfirmed(true);
                return walletTransactionInquiryDTO;
            }
        }
        this.inquiryTransactionFromNetwork(coin, walletTransactionInquiryDTO);
        byCoinAndTransactionId.ifPresent(walletTransaction -> {
            walletTransaction.setConfirmed(walletTransactionInquiryDTO.isConfirmed());
            this.walletTransactionRepository.save(walletTransaction);
        });
        return walletTransactionInquiryDTO;
    }

    @Override
    public PaymentDetails inquiryTransactionByIdAndAddress(Coin coin, String transactionId, String address) {
        return this.coinManagerFactory.getManager(coin).getTransactionPaymentDetails(transactionId, address);
    }

    private void inquiryTransactionFromNetwork(Coin coin, WalletTransactionInquiryDTO walletTransactionInquiryDTO) {
        Boolean result = this.coinManagerFactory.getManager(coin).isConfirmedByTransactionId(walletTransactionInquiryDTO.getTransactionId());
        walletTransactionInquiryDTO.setConfirmed(result);
    }

    private BigDecimal calculateTransactionDisChargeValue(Coin coin, String address, BigDecimal fee) {
        BigDecimal addressBalance = this.coinManagerFactory.getManager(coin).getAddressBalance(address);
        return addressBalance.subtract(fee);
    }

    private Wallet fetchWallet(Coin coin, String address, String privateKey) {
        Optional<Wallet> walletByAddress = walletRepository.findByCoinAndAddress(coin, address);
        if (walletByAddress.isPresent()) {
            return walletByAddress.get();
        } else {
            if (privateKey != null) {
                Wallet wallet = new Wallet();
                wallet.setCoin(coin);
                wallet.setAddress(address);
                wallet.setPrivateKey(privateKey);
                wallet.setCreatedDate(ZonedDateTime.now());
                return this.walletRepository.save(wallet);
            } else throw new CryptoCurrencyApiException(CryptoCurrencyError.WALLET_PRIVATE_KEY_NOT_FOUND);
        }
    }
}
