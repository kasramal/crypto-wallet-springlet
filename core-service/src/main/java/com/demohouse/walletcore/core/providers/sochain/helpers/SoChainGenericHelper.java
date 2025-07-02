package com.demohouse.walletcore.core.providers.sochain.helpers;

import com.demohouse.walletcore.core.providers.sochain.api.SoChainClient;
import com.demohouse.walletcore.core.providers.sochain.api.model.response.*;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class SoChainGenericHelper implements SoChainHelper {


    protected final SoChainClient soChainClient;

    protected SoChainGenericHelper(SoChainClient soChainClient) {
        this.soChainClient = soChainClient;
    }


    @Override
    public BigDecimal getAddressBalance(String address) {
        SoChainAccountBalanceResponse accountBalance = soChainClient.getAccountBalance(this.getCoin(), address);
        return processValue(new BigDecimal(accountBalance.getBalance()));
    }

    @Override
    public Boolean isConfirmed(String address) {
        SoChainAccountBalanceResponse accountBalance = soChainClient.getAccountBalance(this.getCoin(), address);
        BigDecimal unconfirmedBalance = processValue(new BigDecimal(accountBalance.getUnConfirmedBalance()));
        return unconfirmedBalance.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        SoChainTransactionByHashResponse transactionByHash = this.soChainClient.getTransactionByHash(this.getCoin(), transactionId);
        return transactionByHash.getConfirmations() != null && transactionByHash.getConfirmations() >= 1;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        SoChainAccountBalanceResponse accountBalance = soChainClient.getAccountBalance(this.getCoin(), address);
        BigDecimal balance = processValue(new BigDecimal(accountBalance.getBalance()));
        BigDecimal unconfirmedBalance = processValue(new BigDecimal(accountBalance.getUnConfirmedBalance()));

        SoChainTransactionByAddressResponse transactionByAddress = soChainClient.getReceivedTransactionByAddress(this.getCoin(), address);
        List<SoChainTransactionByAddressTransactionResponse> transactions = transactionByAddress.getTransactions();
        boolean isConfirmed = true;
        for (SoChainTransactionByAddressTransactionResponse transaction : transactions) {
            if (transaction.getConfirmations() < 1) {
                isConfirmed = false;
                break;
            }
        }

        String transactionId = null;
        if (!transactions.isEmpty()) {
            transactionId = transactions.get(transactions.size() - 1).getTransactionId();
        }
        return new AccountStatus(isConfirmed, unconfirmedBalance, balance, transactionId);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        SoChainTransactionByHashResponse transaction = soChainClient.getTransactionByHash(this.getCoin(), txHash);
        validatePaymentDetails(transaction);
        BigDecimal fee = BigDecimal.ZERO;
        String buyerAddress = null;
        for (SoChainTransactionByHashUTXOResponse input : transaction.getInputs()) {
            buyerAddress = input.getAddress();
            fee = fee.add(new BigDecimal(input.getValue()));
        }

        BigDecimal receivedValue = BigDecimal.ZERO;
        for (SoChainTransactionByHashUTXOResponse output : transaction.getOutputs()) {
            fee = fee.subtract(new BigDecimal(output.getValue()));
            if (output.getAddress().equals(address))
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

    public BigDecimal processValue(BigDecimal value) {
        return value;
    }

    protected List<GenericUTXO> genericUTXOS(String from) {
        SoChainTransactionByAddressResponse unSpentTransaction = soChainClient.getUnSpentTransactionByAddress(this.getCoin(), from);
        List<GenericUTXO> genericUTXOS = new ArrayList<>();
        for (SoChainTransactionByAddressTransactionResponse transaction : unSpentTransaction.getTransactions()) {
            genericUTXOS.add(new GenericUTXO(transaction.getTransactionId(), transaction.getOutputNumber(), transaction.getScript(), from, new BigDecimal(transaction.getValue())));
        }
        return genericUTXOS;
    }

    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = this.genericUTXOS(from);
        GenericTransaction genericTransaction = new GenericTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }
}
