package com.demohouse.walletcore.core.providers.whatsonchain.helper;

import com.demohouse.walletcore.core.providers.whatsonchain.api.WhatsOnChainClient;
import com.demohouse.walletcore.core.providers.whatsonchain.api.model.response.*;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoinsv.BitcoinSVUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class WhatsOnChainBitcoinSVHelper implements WhatsOnChainHelper {

    private final WhatsOnChainClient whatsOnChainClient;

    public WhatsOnChainBitcoinSVHelper(WhatsOnChainClient whatsOnChainClient) {
        this.whatsOnChainClient = whatsOnChainClient;
    }

    @Override
    public Coin getCoin() {
        return Coin.BITCOINSV;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment cryptoCurrencyPayment = payments.get(0);
        List<WhatsOnChainUnSpentTransactionResponse> unSpentTransactions = this.whatsOnChainClient.getUnSpentTransactions(from);
        List<GenericUTXO> genericUTXOS = new ArrayList<>();
        String script = GenericUtils.generatePublicKeyScript(from);
        for (WhatsOnChainUnSpentTransactionResponse unSpentTransaction : unSpentTransactions) {
            genericUTXOS.add(new GenericUTXO(unSpentTransaction.getTransactionId(), unSpentTransaction.getTransactionPosition(), script, from, BitcoinSVUtils.satoshiToBitcoinSV(unSpentTransaction.getValue())));
        }
        return SerializeTransactionService.serializeBitcoinSVTransaction(privateKey, from, cryptoCurrencyPayment.getTo(), cryptoCurrencyPayment.getValue(), fee, genericUTXOS);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        WhatsOnChainAccountBalanceResponse accountBalance = this.whatsOnChainClient.getAccountBalance(address);
        return this.processValue(accountBalance.getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        WhatsOnChainAccountBalanceResponse accountBalance = this.whatsOnChainClient.getAccountBalance(address);
        BigDecimal unconfirmedBalance = processValue(accountBalance.getUnconfirmed());
        return unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        WhatsOnChainTransactionResponse transactionByHash = this.whatsOnChainClient.getTransactionByHash(transactionId);
        if (transactionByHash.getConfirmations() != null) {
            int compareResult = transactionByHash.getConfirmations().compareTo(BigInteger.ONE);
            return compareResult >= 0;
        }
        return false;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        List<WhatsOnChainTransactionHistoryResponse> transactionHistory = this.whatsOnChainClient.getTransactionHistory(address);

        WhatsOnChainAccountBalanceResponse accountBalance = this.whatsOnChainClient.getAccountBalance(address);
        BigDecimal balance = processValue(accountBalance.getBalance());
        BigDecimal unconfirmedBalance = processValue(accountBalance.getUnconfirmed());
        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;

        String transactionId = null;
        if (!transactionHistory.isEmpty())
            transactionId = transactionHistory.get(0).getTransactionHash();
        return new AccountStatus(
                isConfirmed,
                unconfirmedBalance,
                balance,
                transactionId);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        WhatsOnChainTransactionResponse transactionByHash = this.whatsOnChainClient.getTransactionByHash(txHash);
        BigDecimal fee = BigDecimal.ZERO;
        String buyerAddress = null;
        for (WhatsOnChainTransactionInputResponse input : transactionByHash.getInputs()) {
            WhatsOnChainTransactionResponse inputTransaction = this.whatsOnChainClient.getTransactionByHash(input.getTransactionId());
            for (WhatsOnChainTransactionOutputResponse output : inputTransaction.getOutputs()) {
                if (input.getOutputNumber() == output.getNumber()) {
                    buyerAddress = output.getAddresses().get(0);
                    fee = fee.add(output.getValue());
                }
            }
        }

        BigDecimal receivedValue = BigDecimal.ZERO;
        for (WhatsOnChainTransactionOutputResponse output : transactionByHash.getOutputs()) {
            fee = fee.subtract(output.getValue());
            if (output.getAddresses().contains(address))
                receivedValue = receivedValue.add(output.getValue());
        }

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(receivedValue);
        paymentDetails.setSpentValue(receivedValue.add(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setFee(fee);
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return this.getCoin().convertValue(value);
    }
}
