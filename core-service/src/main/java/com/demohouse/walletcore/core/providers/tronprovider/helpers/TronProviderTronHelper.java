package com.demohouse.walletcore.core.providers.tronprovider.helpers;

import com.demohouse.walletcore.core.providers.tronprovider.api.TronGridApisClient;
import com.demohouse.walletcore.core.providers.tronprovider.api.TronScanApisClient;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.TronScanAccount;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.TronScanTransaction;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.response.TronGridCreateTransactionResponse;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.tron.TronUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.core.transactions.services.sign.entities.TronBlock;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Component
public class TronProviderTronHelper implements TronHelper {

    private final TronScanApisClient tronScanApisClient;
    private final TronGridApisClient tronGridApisClient;

    public TronProviderTronHelper(TronScanApisClient tronScanApisClient, TronGridApisClient tronGridApisClient) {
        this.tronScanApisClient = tronScanApisClient;
        this.tronGridApisClient = tronGridApisClient;
    }

    @Override
    public Coin getCoin() {
        return Coin.TRON;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        try {
            CryptoCurrencyPayment cryptoCurrencyPayment = payments.get(0);
            TronGridCreateTransactionResponse transaction = tronGridApisClient.createTransaction(from, cryptoCurrencyPayment.getTo(), TronUtils.trxToSun(cryptoCurrencyPayment.getValue()).longValue());
            TronBlock tronBlock = new TronBlock();
            tronBlock.setTimestamp(transaction.getRaw_data().getTimestamp());
            tronBlock.setHash("ffffffffffffffff" + transaction.getRaw_data().getRef_block_hash());
            tronBlock.setHeight(BigInteger.valueOf(Long.parseLong(transaction.getRaw_data().getRef_block_bytes(), 16)));
            return SerializeTransactionService.serializeTronTransaction(privateKey, from, cryptoCurrencyPayment.getTo(), cryptoCurrencyPayment.getValue(), BigDecimal.ZERO, tronBlock);
        } catch (Throwable e) {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INSUFFICIENT_BALANCE);
        }
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        TronScanAccount account = tronScanApisClient.getAccount(address);
        return account != null ? this.processValue(BigDecimal.valueOf(account.getBalance())) : BigDecimal.ZERO;
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<TronScanTransaction> transactions = tronScanApisClient.getTransactions(address);
        BigDecimal unconfirmedBalance = calculateUnconfirmedBalance(address, transactions);
        return unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;
    }

    private BigDecimal calculateUnconfirmedBalance(String address, List<TronScanTransaction> transactions) {
        BigDecimal unconfirmedBalance = BigDecimal.ZERO;
        for (TronScanTransaction tx : transactions) {
            if (!tx.isConfirmed()) {
                if (tx.getToAddress().equals(address))
                    unconfirmedBalance = unconfirmedBalance.add(new BigDecimal(tx.getAmount()));
                else
                    unconfirmedBalance = unconfirmedBalance.subtract(new BigDecimal(tx.getAmount()));
            }
        }
        return unconfirmedBalance;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        TronScanTransaction transactionInfo = tronScanApisClient.getTransactionInfo(transactionId);
        return transactionInfo != null && transactionInfo.isConfirmed();
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        TronScanAccount account = tronScanApisClient.getAccount(address);
        List<TronScanTransaction> transactions = tronScanApisClient.getTransactions(address);
        BigDecimal unconfirmedBalance = calculateUnconfirmedBalance(address, transactions);
        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;

        String txId = null;
        if (!transactions.isEmpty())
            txId = transactions.get(0).getHash();
        return new AccountStatus(
                isConfirmed,
                processValue(unconfirmedBalance),
                processValue(new BigDecimal(account.getBalance())),
                txId);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        TronScanTransaction transactionInfo = tronScanApisClient.getTransactionInfo(txHash);
        if (transactionInfo != null) {
            if (transactionInfo.getOwnerAddress().equals(address) || transactionInfo.getToAddress().equals(address)) {
                PaymentDetails paymentDetails = new PaymentDetails();
                paymentDetails.setTxId(transactionInfo.getHash());
                paymentDetails.setBuyerAddress(transactionInfo.getToAddress());
                paymentDetails.setReceivedValue(this.processValue(new BigDecimal(transactionInfo.getContractData().getAmount())));
                paymentDetails.setSpentValue(this.processValue(new BigDecimal(transactionInfo.getContractData().getAmount())));
                paymentDetails.setFee(BigDecimal.ZERO);
                return paymentDetails;
            } else {
                throw new CryptoCurrencyApiException(CryptoCurrencyError.TRANSACTION_OWNER_ADDRESS_NOT_VALID);
            }
        } else {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.TRANSACTION_NOT_FOUND);
        }
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return this.getCoin().convertValue(value);
    }
}
