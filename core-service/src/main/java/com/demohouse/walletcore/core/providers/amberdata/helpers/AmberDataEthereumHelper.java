package com.demohouse.walletcore.core.providers.amberdata.helpers;

import com.demohouse.walletcore.core.providers.BalanceCalculationMode;
import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataAccountTransactions;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataAddressInfo;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTransaction;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumAddress;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransaction;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AmberDataEthereumHelper implements AmberDataHelper {

    private static final Coin COIN = Coin.ETHEREUM;

    private final AmberDataClient amberDataClient;

    public AmberDataEthereumHelper(AmberDataClient amberDataClient) {
        this.amberDataClient = amberDataClient;
    }


    private EthereumAddress getAddress(String address) {
        AmberDataAddressInfo amberDataAddressInfo = amberDataClient.getAddress(Coin.ETHEREUM, address.toLowerCase());
        EthereumAddress ethereumAddress = new EthereumAddress();
        ethereumAddress.setBalance(processValue(amberDataAddressInfo.getBalance()));
        return ethereumAddress;
    }

    public BigDecimal getNonce(String address) {
        BigDecimal nonce = BigDecimal.ZERO;

        AmberDataAccountTransactions amberDataAccountTransactions = amberDataClient.getTransactionOfAddress(Coin.ETHEREUM, address.toLowerCase());

        if (amberDataAccountTransactions.getRecords().size() == 0) return nonce;
        for (AmberDataTransaction transaction : amberDataAccountTransactions.getRecords()) {
            if (transaction.getFrom().get(0).getAddress().equals(address.toLowerCase()))
                nonce = nonce.add(BigDecimal.ONE);
        }

        return nonce;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment payment = payments.get(0);
        EthereumAddress ethereumAddress = getAddress(from);
        ethereumAddress.setNonce(getNonce(from));
        EthereumTransaction transaction = EthereumTransactionBuilder.build(ethereumAddress, payment.getTo(), payment.getValue(), fee);
        return "0x" + transaction.sign(privateKey, true);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        return processValue(getAddress(address).getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        AmberDataAccountTransactions pendingTransactionsOfAddress = amberDataClient.getPendingTransactionOfEthereumAddress(getCoin(), address.toLowerCase());
        return pendingTransactionsOfAddress.getRecords().isEmpty();
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        AmberDataTransaction transaction = this.amberDataClient.getTransaction(this.getCoin(), transactionId);
        return transaction != null && transaction.getConfirmations() != null && transaction.getConfirmations().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        AmberDataAccountTransactions transactionOfAddress = amberDataClient.getTransactionOfAddress(getCoin(), address.toLowerCase());

        List<AmberDataTransaction> pendingTransactions = transactionOfAddress
                .getRecords()
                .stream()
                .filter((transaction) -> transaction.getConfirmations() == null || transaction.getConfirmations().compareTo(BigDecimal.ZERO) == 0)
                .collect(Collectors.toList());

        BigDecimal balance = getAddress(address).getBalance();
        BigDecimal unconfirmedBalance = calculateBalanceFromTransactions(address, pendingTransactions);

        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;
        String transactionId = null;
        if (isConfirmed) {
            AmberDataAccountTransactions amberDataAccountTransactions = amberDataClient.getTransactionOfAddress(getCoin(), address.toLowerCase());
            if (!amberDataAccountTransactions.getRecords().isEmpty())
                transactionId = amberDataAccountTransactions.getRecords().get(amberDataAccountTransactions.getRecords().size() - 1).getHash();
        } else {
            if (!pendingTransactions.isEmpty())
                transactionId = pendingTransactions.get(pendingTransactions.size() - 1).getHash();
        }
        return new AccountStatus(
                isConfirmed,
                unconfirmedBalance,
                balance,
                transactionId,
                BalanceCalculationMode.CONFIRMED_BALANCE_CALCULATION);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        AmberDataTransaction transaction = amberDataClient.getTransaction(Coin.ETHEREUM, txHash);
        validatePaymentDetails(transaction);

        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = transaction.getFee();
        BigDecimal receivedValue = transaction.getValue();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue.add(fee)));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(transaction.getFrom().get(0).getAddress());
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return EthereumUtils.convertWeiToEther(value);
    }

    public BigDecimal calculateBalanceFromTransactions(String address, List<AmberDataTransaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;
        for (AmberDataTransaction transaction : transactions) {
            if (transaction.getFrom().get(0).getAddress().equals(address.toLowerCase()))
                balance = balance.subtract(transaction.getValue());
            if (transaction.getTo().get(0).getAddress().equals(address.toLowerCase()))
                balance = balance.add(transaction.getValue());
        }
        return processValue(balance);
    }
}
