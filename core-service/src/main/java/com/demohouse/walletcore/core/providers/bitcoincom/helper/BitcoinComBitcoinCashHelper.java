package com.demohouse.walletcore.core.providers.bitcoincom.helper;

import com.demohouse.walletcore.core.providers.bitcoincom.api.BitcoinComClient;
import com.demohouse.walletcore.core.providers.bitcoincom.api.model.response.*;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BitcoinComBitcoinCashHelper implements BitcoinComHelper {

    private final BitcoinComClient bitcoinComClient;

    public BitcoinComBitcoinCashHelper(BitcoinComClient bitcoinComClient) {
        this.bitcoinComClient = bitcoinComClient;
    }

    @Override
    public Coin getCoin() {
        return Coin.BITCOINCASH;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment cryptoCurrencyPayment = payments.get(0);
        BitcoinComAccountUTXOsResponse accountUTXOs = bitcoinComClient.getAccountUTXOs(from);
        List<GenericUTXO> genericUTXOS = new ArrayList<>();
        for (BitcoinComAccountUTXOsItemResponse item : accountUTXOs.getItems()) {
            genericUTXOS.add(new GenericUTXO(item.getTransactionId(), item.getOutputNumber(), accountUTXOs.getScript(), BitcoinCashUtils.getP2PkhAddress(from), item.getAmount()));
        }
        return SerializeTransactionService.serializeBitcoinCashTransaction(privateKey, BitcoinCashUtils.getP2PkhAddress(from), BitcoinCashUtils.getP2PkhAddress(cryptoCurrencyPayment.getTo()), cryptoCurrencyPayment.getValue(), fee, genericUTXOS);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        BitcoinComAccountBalanceResponse accountBalance = bitcoinComClient.getAccountBalance(address);
        return this.processValue(accountBalance.getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<BitcoinComTransactionResponse> transactionsByAddress = bitcoinComClient.getTransactionsByAddress(address);
        for (BitcoinComTransactionResponse byAddress : transactionsByAddress) {
            if (byAddress == null || byAddress.getConfirmations() < 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        BitcoinComTransactionResponse transactionByHash = this.bitcoinComClient.getTransactionByHash(transactionId);
        return transactionByHash.getConfirmations() != null && transactionByHash.getConfirmations() >= 1;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        List<BitcoinComTransactionResponse> transactionsByAddress = bitcoinComClient.getTransactionsByAddress(address);
        boolean isConfirmed = true;
        for (BitcoinComTransactionResponse byAddress : transactionsByAddress) {
            if (byAddress == null || byAddress.getConfirmations() < 1) {
                isConfirmed = false;
                break;
            }
        }
        String transactionId = null;
        if (!transactionsByAddress.isEmpty())
            transactionId = transactionsByAddress.get(0).getTransactionId();
        BitcoinComAccountBalanceResponse accountBalance = this.bitcoinComClient.getAccountBalance(address);
        BigDecimal balance = processValue(accountBalance.getBalance());
        BigDecimal unconfirmedBalance = processValue(accountBalance.getUnconfirmedBalance());
        return new AccountStatus(isConfirmed, unconfirmedBalance, balance, transactionId);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BitcoinComTransactionResponse transactionByHash = this.bitcoinComClient.getTransactionByHash(txHash);
        validatePaymentDetails(transactionByHash);
        BigDecimal fee = BigDecimal.ZERO;
        String buyerAddress = null;
        for (BitcoinComTransactionInputResponse input : transactionByHash.getInputs()) {
            buyerAddress = input.getAddress();
            fee = fee.add(input.getValue());
        }

        BigDecimal receivedValue = BigDecimal.ZERO;
        for (BitcoinComTransactionOutputResponse output : transactionByHash.getOutputs()) {
            fee = fee.subtract(new BigDecimal(output.getValue()));
            if (output.getAddresses().contains(address))
                receivedValue = receivedValue.add(new BigDecimal(output.getValue()));
        }

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(this.processValue(receivedValue));
        paymentDetails.setSpentValue(this.processValue(receivedValue.add(fee)));
        paymentDetails.setTxId(txHash);
        paymentDetails.setFee(this.processValue(fee));
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return value;
    }
}
