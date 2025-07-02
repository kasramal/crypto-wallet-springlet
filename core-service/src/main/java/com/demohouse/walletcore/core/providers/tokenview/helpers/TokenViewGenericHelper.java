package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.core.providers.tokenview.api.models.*;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TokenViewGenericHelper implements TokenViewHelper {


    protected final TokenViewClient tokenViewClient;

    public TokenViewGenericHelper(TokenViewClient tokenViewClient) {
        this.tokenViewClient = tokenViewClient;
    }

    public String generatePublicKeyScript(String address) {
        return GenericUtils.generatePublicKeyScript(address);
    }

    public List<GenericUTXO> getUTXOs(String address) {
        List<TokenViewUTXO> uTXOs = tokenViewClient.getAddressUTXOs(getCoin(), processAddress(address));
        List<GenericUTXO> genericUTXOS = new ArrayList<>();
        String scriptPublicKey = generatePublicKeyScript(address);
        uTXOs.forEach(utxo -> genericUTXOS.add(new GenericUTXO(
                utxo.getTxid(),
                utxo.getOutputNo(),
                scriptPublicKey,
                address,
                processValue(utxo.getValue())
        )));
        return genericUTXOS;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new GenericTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        TokenViewAddressInfo tokenViewAddressInfo = tokenViewClient.getAddressWithoutContractId(getCoin(), processAddress(address));
        BigDecimal balance =
                tokenViewAddressInfo.getBalance() != null
                        ? tokenViewAddressInfo.getBalance()
                        : tokenViewAddressInfo.getReceive().add(tokenViewAddressInfo.getSpend());
        return processValue(balance);
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<TokenViewTransaction> pendingTransactionsOfAddress = tokenViewClient.getPendingTransactionsOfAddress(getCoin(), processAddress(address));

        return pendingTransactionsOfAddress.isEmpty();
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        TokenViewTransaction transaction = this.tokenViewClient.getTransaction(this.getCoin(), transactionId);
        return transaction != null && transaction.getConfirmations() != null && transaction.getConfirmations() >= 1;
    }

    @Override
    // isConfirmed would be set true after the very first confirmation
    public AccountStatus getTransactionStatus(String address) {
        TokenViewAddressInfo tokenViewAddressInfo = tokenViewClient.getAddressWithoutContractId(getCoin(), processAddress(address));
        List<TokenViewTransaction> confirmedTransactions = tokenViewAddressInfo.getTxs();
        List<TokenViewTransaction> pendingTransactionsOfAddress = tokenViewClient.getPendingTransactionsOfAddress(getCoin(), processAddress(address));

        boolean isConfirmed = pendingTransactionsOfAddress.isEmpty();

        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal unconfirmedBalance = BigDecimal.ZERO;
        List<TokenViewTransaction> transactions = new ArrayList<>();
        transactions.addAll(pendingTransactionsOfAddress);
        transactions.addAll(confirmedTransactions);

        for (TokenViewTransaction transaction : transactions) {
            BigDecimal sum = BigDecimal.ZERO;
            for (TokenViewTransactionOutput output : transaction.getOutputs()) {
                if (output.getAddress().equals(processAddress(address))) {
                    sum = sum.add(output.getValue());
                }
            }
            for (TokenViewTransactionInput input : transaction.getInputs()) {
                if (input.getAddress().equals(processAddress(address))) {
                    sum = sum.subtract(input.getValue());
                }
            }
            if (transaction.getConfirmations() != null && transaction.getConfirmations() > 0) {
                balance = balance.add(sum); // TODO What should we do if transactions` list is large?
            } else {
                unconfirmedBalance = unconfirmedBalance.add(sum);
            }
        }

        balance = processValue(balance);
        unconfirmedBalance = processValue(unconfirmedBalance);
        String transactionHash = isConfirmed ? confirmedTransactions.get(0).getTxid() : pendingTransactionsOfAddress.get(0).getTxid();
        return new AccountStatus(isConfirmed, unconfirmedBalance, balance, transactionHash);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        TokenViewTransaction tx = tokenViewClient.getTransaction(getCoin(), txHash);
        BigDecimal fee = tx.getFee();
        BigDecimal receivedValue = BigDecimal.ZERO;
        for (TokenViewTransactionOutput output : tx.getOutputs()) {
            if (output.getAddress().equals(address))
                receivedValue = receivedValue.add(output.getValue());
        }
        String buyerAddress = null;
        for (TokenViewTransactionInput input : tx.getInputs())
            buyerAddress = input.getAddress();


        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue.add(fee)));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    public String processAddress(String address) {
        return address;
    }

    public BigDecimal processValue(BigDecimal value) {
        return value;
    }

    protected List<GenericUTXO> getGeneratedUTXOs(String address) {
        List<TokenViewTransactionOutput> outputs = new ArrayList<>();
        List<TokenViewTransactionInput> inputs = new ArrayList<>();
        this.generateInputAndOutputs(address, outputs, inputs, 1);
        List<TokenViewTransactionOutput> filteredOutputs = outputs
                .stream()
                .filter(tokenViewTransactionOutput -> {
                    boolean shouldFilter = true;
                    for (TokenViewTransactionInput input : inputs) {
                        if (tokenViewTransactionOutput.getTransactionId().equals(input.getTxid())) {
                            if (tokenViewTransactionOutput.getOutputNo() != null && tokenViewTransactionOutput.getOutputNo().equals((long) input.getOutputNo()))
                                shouldFilter = false;
                        }
                    }
                    return shouldFilter;
                })
                .collect(Collectors.toList());
        List<GenericUTXO> genericUTXOS = new ArrayList<>();
        String script = GenericUtils.generatePublicKeyScript(address);
        for (TokenViewTransactionOutput filteredOutput : filteredOutputs) {
            genericUTXOS.add(new GenericUTXO(filteredOutput.getTransactionId(), filteredOutput.getOutputNo(), script, address, filteredOutput.getValue()));
        }
        return genericUTXOS;
    }

    private void generateInputAndOutputs(String address, List<TokenViewTransactionOutput> outputs, List<TokenViewTransactionInput> inputs, int pageNumber) {
        TokenViewAddressInfo transactionsByAddress = this.tokenViewClient.getTransactionsByAddress(this.getCoin(), address, pageNumber, 50);
        for (TokenViewTransaction tx : transactionsByAddress.getTxs()) {
            for (TokenViewTransactionInput input : tx.getInputs()) {
                if (address.equals(input.getAddress())) {
                    inputs.add(input);
                }
            }
            for (TokenViewTransactionOutput output : tx.getOutputs()) {
                if (address.equals(output.getAddress())) {
                    output.setTransactionId(tx.getTxid());
                    outputs.add(output);
                }
            }
        }
        if (transactionsByAddress.getTransactionCount() > 50) {
            this.generateInputAndOutputs(address, outputs, inputs, ++pageNumber);
        }
    }
}
