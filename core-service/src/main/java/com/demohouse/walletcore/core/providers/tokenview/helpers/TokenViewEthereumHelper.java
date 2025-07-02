package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewAddressInfo;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewTransaction;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumAddress;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransaction;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransactionBuilder;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TokenViewEthereumHelper implements TokenViewHelper {

    private static final Coin COIN = Coin.ETHEREUM;

    private final TokenViewClient tokenViewClient;

    public TokenViewEthereumHelper(TokenViewClient tokenViewClient) {
        this.tokenViewClient = tokenViewClient;
    }


    public BigDecimal countOutgoingPendingTransactions(String address) {
        int txCont = 0;
        List<TokenViewTransaction> pendingTransactionsOfAddress = tokenViewClient.getPendingTransactionsOfAddress(Coin.ETHEREUM, address.toLowerCase());
        for (TokenViewTransaction transaction : pendingTransactionsOfAddress) {
            if (transaction.getFrom().equals(address.toLowerCase()))
                txCont++;
        }
        return BigDecimal.valueOf(txCont);
    }

    private EthereumAddress getAddress(String address) {
        TokenViewAddressInfo tokenViewAddressInfo = tokenViewClient.getAddressWithContractId(getCoin(), address.toLowerCase());
        EthereumAddress ethereumAddress = new EthereumAddress();
        ethereumAddress.setBalance(processValue(tokenViewAddressInfo.getBalance()));
        ethereumAddress.setNonce(tokenViewAddressInfo.getNonce().add(countOutgoingPendingTransactions(address)));
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
        TokenViewAddressInfo transactionsByAddress = tokenViewClient.getAddressWithContractId(getCoin(), address.toLowerCase());
        if (transactionsByAddress.getTxs() == null || transactionsByAddress.getTxs().size() == 0)
            return false;

        List<TokenViewTransaction> pendingTransactionsOfAddress = tokenViewClient.getPendingTransactionsOfAddress(getCoin(), address.toLowerCase());
        return pendingTransactionsOfAddress.isEmpty();
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        TokenViewTransaction transaction = tokenViewClient.getTransaction(getCoin(), transactionId);
        return transaction != null && transaction.getConfirmations() > 1;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        TokenViewAddressInfo transactionsByAddress = tokenViewClient.getAddressWithContractId(getCoin(), address.toLowerCase());

        List<TokenViewTransaction> transactions = transactionsByAddress.getTxs();
        List<TokenViewTransaction> pendingTransactionsOfAddress = tokenViewClient.getPendingTransactionsOfAddress(getCoin(), address.toLowerCase());

        BigDecimal balance = processValue(transactionsByAddress.getBalance());
        BigDecimal unconfirmedBalance = calculateBalanceFromTransactions(address, pendingTransactionsOfAddress);
        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;

        String txId = null;
        if (isConfirmed && !transactions.isEmpty())
            txId = transactions.get(0).getTxid();
        else if (!pendingTransactionsOfAddress.isEmpty())
            txId = pendingTransactionsOfAddress.get(0).getTxid();

        return new AccountStatus(
                isConfirmed,
                unconfirmedBalance,
                balance,
                txId);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        TokenViewTransaction transaction = tokenViewClient.getTransaction(getCoin(), txHash);
        validatePaymentDetails(transaction);
        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = transaction.getFee();
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
        return value;
    }

    public BigDecimal calculateBalanceFromTransactions(String address, List<TokenViewTransaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;
        for (TokenViewTransaction transaction : transactions) {
            if (transaction.getFrom().equals(address.toLowerCase()))
                balance = balance.subtract(transaction.getValue().add(transaction.getFee()));
            if (transaction.getTo().equals(address.toLowerCase()))
                balance = balance.add(transaction.getValue());
        }
        return processValue(balance);
    }
}
