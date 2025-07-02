package com.demohouse.walletcore.core.providers.cryptoapis.helpers;

import com.demohouse.walletcore.core.providers.cryptoapis.api.CryptoApisClient;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransaction;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransactionInput;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransactionOutput;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CryptoApisGenericHelper implements CryptoApisHelper {

    private final CryptoApisClient cryptoApisClient;

    public CryptoApisGenericHelper(CryptoApisClient cryptoApisClient) {
        this.cryptoApisClient = cryptoApisClient;
    }

    public String preProcessAddress(String address) {
        return address;
    }

    private void extractUTXOsFromOutputs(String address, CryptoApisTransaction transaction, List<GenericUTXO> utxos) {
        for (int index = 0; index < transaction.getTxouts().size(); index++) {
            CryptoApisTransactionOutput output = transaction.getTxouts().get(index);

            if (!output.getSpent() && output.getAddresses().get(0).equals(preProcessAddress(address))) {

                GenericUTXO utxo = new GenericUTXO(
                        transaction.getTxid(),
                        index,
                        output.getScript().getHex(),
                        address,
                        output.getAmount()
                );
                utxos.add(utxo);
            }
        }
    }

    public List<GenericUTXO> getUTXOs(String address) {
        List<GenericUTXO> utxos = new ArrayList<>();

        List<CryptoApisTransaction> unconfirmedTransactions = cryptoApisClient.getUnconfirmedTransactions(getCoin(), address);
        List<CryptoApisTransaction> confirmedTransactions = cryptoApisClient.getConfirmedTransactions(getCoin(), address);

        List<CryptoApisTransaction> transactions = new ArrayList<>();
        transactions.addAll(confirmedTransactions);
        transactions.addAll(unconfirmedTransactions);

        for (CryptoApisTransaction transaction : transactions)
            extractUTXOsFromOutputs(address, transaction, utxos);

        List<GenericUTXO> collect = utxos.stream().filter(utxo -> {
            for (CryptoApisTransaction transaction : transactions) {
                for (CryptoApisTransactionInput input : transaction.getTxins()) {
                    if (input.getTxout().equals(utxo.getTransactionHash()))
                        if (input.getAmount().compareTo(utxo.getValue()) == 0)
                            return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new GenericTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        return cryptoApisClient.getAddress(getCoin(), address).getBalance();
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<CryptoApisTransaction> transactions = cryptoApisClient.getUnconfirmedTransactions(getCoin(), address);
        return transactions.isEmpty();
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        CryptoApisTransaction transaction = null;
        try {
            transaction = cryptoApisClient.getTransactions(getCoin(), transactionId);
        } catch (WebClientResponseException exception) {
            if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)) return false;
        }
        return transaction != null && transaction.getConfirmations().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    // isConfirmed would be set true after the very first confirmation
    public AccountStatus getTransactionStatus(String address) {
        List<CryptoApisTransaction> unconfirmedTransactions = cryptoApisClient.getUnconfirmedTransactions(getCoin(), address);
        boolean isConfirmed = unconfirmedTransactions.isEmpty();
        List<CryptoApisTransaction> transactions = cryptoApisClient.getConfirmedTransactions(getCoin(), address);

        BigDecimal balance = calculateBalanceFromTransactions(address, transactions); // TODO What should we do if transactions` list is large?
        BigDecimal unconfirmedBalance = calculateBalanceFromTransactions(address, unconfirmedTransactions);

        List<CryptoApisTransaction> txs = (isConfirmed ? transactions : unconfirmedTransactions);
        String transactionHash = null;
        if (!txs.isEmpty())
            transactionHash = txs.get(0).getTxid();
        return new AccountStatus(isConfirmed, unconfirmedBalance, balance, transactionHash);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        CryptoApisTransaction transaction = null;
        try {
            transaction = cryptoApisClient.getTransactions(getCoin(), txHash);
        } catch (WebClientResponseException exception) {
            if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)) return new PaymentDetails();
        }

        BigDecimal received = transaction.getReceived().get(address);
        BigDecimal fee = transaction.getFee();

        String buyerAddress = null;
        for (String addr : transaction.getSent().keySet())
            buyerAddress = addr;

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(received);
        paymentDetails.setSpentValue(received.add(fee));
        paymentDetails.setFee(fee);
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    public BigDecimal processValue(BigDecimal value) {
        return value;
    }

    public BigDecimal calculateBalanceFromTransactions(String address, List<CryptoApisTransaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;
        for (CryptoApisTransaction transaction : transactions) {
            for (CryptoApisTransactionOutput output : transaction.getTxouts()) {
                if (output.getAddresses().contains(preProcessAddress(address))) {
                    balance = balance.add(output.getAmount());
                }
            }
            for (CryptoApisTransactionInput input : transaction.getTxins()) {
                if (input.getAddresses().contains(preProcessAddress(address))) {
                    balance = balance.subtract(input.getAmount());
                }
            }
        }
        return processValue(balance);
    }
}
