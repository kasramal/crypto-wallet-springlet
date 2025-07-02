package com.demohouse.walletcore.core.providers.amberdata.helpers;

import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataAddressInfo;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTransaction;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTransactionInput;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTransactionOutput;
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

public abstract class AmberDataGenericHelper implements AmberDataHelper {


    private final AmberDataClient amberDataClient;

    public AmberDataGenericHelper(AmberDataClient amberDataClient) {
        this.amberDataClient = amberDataClient;
    }

    public String generatePublicKeyScript(String address) {
        return GenericUtils.generatePublicKeyScript(address);
    }

    public List<GenericUTXO> getUTXOs(String address) {
        List<AmberDataTransaction> transactions = amberDataClient.getTransactionOfAddress(getCoin(), address).getRecords();
        List<AmberDataTransactionInput> inputs = new ArrayList<>();
        List<AmberDataTransactionOutput> outputs = new ArrayList<>();
        for (AmberDataTransaction tx : transactions) {

            for (AmberDataTransactionInput input : tx.getInputs()) {
                if (input.getAddresses().contains(address)) {
                    inputs.add(input);
                }
            }
            for (AmberDataTransactionOutput output : tx.getOutputs()) {
                if (output.getAddresses().contains(address)) {
                    outputs.add(output);
                }
            }
        }

        List<AmberDataTransactionOutput> filteredOutputs = outputs
                .stream()
                .filter(amberDataTransactionOutput -> {
                    boolean shouldFilter = true;
                    for (AmberDataTransactionInput input : inputs) {
                        if (amberDataTransactionOutput.getTransactionHash().equals(input.getOutputTransactionHash())) {
                            if (amberDataTransactionOutput.getIndex() != null && amberDataTransactionOutput.getIndex().equals(input.getOutputIndex()))
                                shouldFilter = false;
                        }
                    }
                    return shouldFilter;
                })
                .collect(Collectors.toList());

        List<GenericUTXO> genericUTXOS = new ArrayList<>();
        String scriptPublicKey = generatePublicKeyScript(address);
        filteredOutputs.forEach(utxo -> genericUTXOS.add(new GenericUTXO(
                utxo.getTransactionHash(),
                utxo.getIndex(),
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
        AmberDataAddressInfo amberDataAddressInfo = amberDataClient.getAddress(getCoin(), address);
        return processValue(amberDataAddressInfo.getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<AmberDataTransaction> pendingTransactions = amberDataClient.getPendingTransactionsOfAddress(getCoin(), address).getRecords();
        List<AmberDataTransaction> confirmedTransactions = amberDataClient.getTransactionOfAddress(getCoin(), address).getRecords();
        return pendingTransactions.isEmpty() && !confirmedTransactions.isEmpty();
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        AmberDataTransaction transaction = this.amberDataClient.getTransaction(this.getCoin(), transactionId);
        return transaction != null && transaction.getStatusResult().getConfirmed() && transaction.getStatusResult().getSuccess();
    }

    @Override
    // isConfirmed would be set true after the very first confirmation
    public AccountStatus getTransactionStatus(String address) {
        List<AmberDataTransaction> pendingTransactions = amberDataClient.getPendingTransactionsOfAddress(getCoin(), address).getRecords();
        List<AmberDataTransaction> confirmedTransactions = amberDataClient.getTransactionOfAddress(getCoin(), address).getRecords(); // TODO What should we do if transactions` list is large?

        boolean isConfirmed = pendingTransactions.isEmpty();
        List<AmberDataTransaction> list = (isConfirmed ? confirmedTransactions : pendingTransactions);
        String txHash = null;
        if (!list.isEmpty())
            txHash = list.get(list.size() - 1).getHash();

        BigDecimal balance = calculateBalanceFromTransactions(address, confirmedTransactions);
        BigDecimal unconfirmedBalance = calculateBalanceFromTransactions(address, pendingTransactions);
        return new AccountStatus(
                isConfirmed,
                unconfirmedBalance,
                balance,
                txHash);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        AmberDataTransaction transaction = amberDataClient.getTransaction(getCoin(), txHash);
        validatePaymentDetails(transaction);

        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = transaction.getFee();
        BigDecimal receivedValue = transaction.getValue();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue.add(fee)));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(processAddress(transaction.getFrom().get(0).getAddress()));
        return paymentDetails;
    }

    public BigDecimal processValue(BigDecimal value) {
        return getCoin().convertValue(value);
    }

    public String processAddress(String address) {
        return address;
    }

    public BigDecimal calculateBalanceFromTransactions(String address, List<AmberDataTransaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;
        for (AmberDataTransaction transaction : transactions) {
            for (AmberDataTransactionOutput output : transaction.getOutputs()) {
                if (output.getAddresses().contains(processAddress(address))) {
                    balance = balance.add(output.getValue());
                }
            }
            for (AmberDataTransactionInput input : transaction.getInputs()) {
                if (input.getAddresses().contains(processAddress(address))) {
                    balance = balance.subtract(input.getValue());
                }
            }
        }
        return processValue(balance);
    }
}
