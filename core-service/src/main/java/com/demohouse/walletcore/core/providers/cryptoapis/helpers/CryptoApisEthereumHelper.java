package com.demohouse.walletcore.core.providers.cryptoapis.helpers;

import com.demohouse.walletcore.core.providers.BalanceCalculationMode;
import com.demohouse.walletcore.core.providers.cryptoapis.api.CryptoApisClient;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisAddress;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransaction;
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
public class CryptoApisEthereumHelper implements CryptoApisHelper {

    private static final Coin COIN = Coin.ETHEREUM;

    private final CryptoApisClient cryptoApisClient;

    public CryptoApisEthereumHelper(CryptoApisClient cryptoApisClient) {
        this.cryptoApisClient = cryptoApisClient;
    }

    private EthereumAddress getAddress(String address) {
        CryptoApisAddress cryptoApisAddress = cryptoApisClient.getAddress(getCoin(), address);
        EthereumAddress ethereumAddress = new EthereumAddress();
        ethereumAddress.setBalance(cryptoApisAddress.getBalance());
        BigDecimal addressNonce = cryptoApisClient.getAddressNonce(getCoin(), address).getNonce();
        ethereumAddress.setNonce(addressNonce);
        return ethereumAddress;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment payment = payments.get(0);
        EthereumAddress ethereumAddress = getAddress(from);
        EthereumTransaction transaction = EthereumTransactionBuilder.build(ethereumAddress, payment.getTo(), payment.getValue(), fee);
        return "0x" + transaction.sign(privateKey, true);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        return getAddress(address).getBalance();
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<CryptoApisTransaction> unconfirmedTransactions = cryptoApisClient.getUnconfirmedTransactions(getCoin(), address);

        BigDecimal unconfirmedBalance = calculateBalanceFromTransactions(address, unconfirmedTransactions);
        return unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        CryptoApisTransaction transaction = cryptoApisClient.getTransactionByHash(getCoin(), transactionId);
        return transaction != null && transaction.getConfirmations().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        EthereumAddress ethereumAddress = getAddress(address);
        List<CryptoApisTransaction> transactions = cryptoApisClient.getConfirmedTransactions(getCoin(), address);

        BigDecimal balance = ethereumAddress.getBalance();
        BigDecimal unconfirmedBalance =
                calculateBalanceFromTransactions(address, transactions
                        .stream()
                        .filter((tx -> tx.getConfirmations() == null || tx.getConfirmations().equals(BigDecimal.ZERO)))
                        .collect(Collectors.toList()));
        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;

        String txId = null;
        if (isConfirmed && !transactions.isEmpty())
            txId = transactions.get(0).getHash();

        return new AccountStatus(
                isConfirmed,
                unconfirmedBalance,
                balance,
                txId,
                BalanceCalculationMode.CONFIRMED_BALANCE_CALCULATION);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        CryptoApisTransaction transaction = cryptoApisClient.getTransactionByHash(getCoin(), txHash);
        validatePaymentDetails(transaction);
        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = transaction.getGasPrice().multiply(transaction.getGasUsed());
        BigDecimal receivedValue = transaction.getValue();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue.add(fee)));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(transaction.getFrom());
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return EthereumUtils.convertWeiToEther(value);
    }

    public BigDecimal calculateBalanceFromTransactions(String address, List<CryptoApisTransaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;
        for (CryptoApisTransaction transaction : transactions) {
            if (transaction.getFrom().equals(address.toLowerCase()))
                balance = balance.subtract(transaction.getValue());
            if (transaction.getTo().equals(address.toLowerCase()))
                balance = balance.add(transaction.getValue());
        }
        return processValue(balance);
    }
}
