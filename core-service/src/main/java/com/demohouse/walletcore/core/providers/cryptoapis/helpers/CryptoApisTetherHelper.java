package com.demohouse.walletcore.core.providers.cryptoapis.helpers;

import com.demohouse.walletcore.core.providers.cryptoapis.api.CryptoApisClient;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisAddressToken;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisAddressTokenTransfer;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransaction;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Address;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Token;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Transaction;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20TransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.tether.TetherUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

//@Component
public class CryptoApisTetherHelper implements CryptoApisHelper {

    private static final Coin COIN = Coin.TETHER;

    private final CryptoApisClient cryptoApisClient;

    public CryptoApisTetherHelper(CryptoApisClient cryptoApisClient) {
        this.cryptoApisClient = cryptoApisClient;
    }


    private Erc20Token getAddressTetherTokens(String address) {
        List<CryptoApisAddressTokenTransfer> cryptoApisAddressTokenTransfers = cryptoApisClient.getAddressTokenTransfers(COIN, address);
        Erc20Token token = new Erc20Token();
        BigDecimal balance = BigDecimal.ZERO;
        token.setTokenSymbol(COIN.getIso());

        if (cryptoApisAddressTokenTransfers == null)
            return token;

        if (cryptoApisAddressTokenTransfers.size() > 0)
            token.setTokenAddress(cryptoApisAddressTokenTransfers.get(0).getTxHash());

        for (CryptoApisAddressTokenTransfer tokenItt : cryptoApisAddressTokenTransfers) {
            if (tokenItt.getSymbol().equals(COIN.getIso())) {
                balance = balance.add(tokenItt.getValue());
            }
        }
        return token;
    }

    private String getTokenAddress(String address) {

        List<CryptoApisAddressToken> cryptoApisAddressTokens = cryptoApisClient.getAddressTokens(COIN, address);
        for (CryptoApisAddressToken token : cryptoApisAddressTokens) {
            if (token.getSymbol().equals(COIN.getIso())) return token.getContract();
        }
        return "";
    }

    private Erc20Address getAddress(String address) {

        BigDecimal nonce = cryptoApisClient.getAddressNonce(COIN, address).getNonce();
        Erc20Token token = getAddressTetherTokens(address);
        String tokenAddress = getTokenAddress(address);
        token.setTokenAddress(tokenAddress);

        Erc20Address erc20Address = new Erc20Address();
        erc20Address.setTokens(Collections.singletonList(token));
        erc20Address.setNonce(nonce == null ? new BigDecimal("0") : nonce);

        return erc20Address;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment payment = payments.get(0);
        Erc20Address erc20Address = getAddress(from);
        Erc20Transaction transaction = Erc20TransactionBuilder.build(COIN, erc20Address, payment.getTo(), payment.getValue(), fee);
        return "0x" + transaction.sign(privateKey, true);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        Erc20Token token = getAddressTetherTokens(address);
        return processValue(token.getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        Erc20Token token = getAddressTetherTokens(address);
        return token.getBalance().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        CryptoApisTransaction cryptoApisTransaction = cryptoApisClient.getTransactionByHash(COIN, transactionId);
        return cryptoApisTransaction.getConfirmations().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        Erc20Token token = getAddressTetherTokens(address);
        if (token.getBalance().compareTo(BigDecimal.ZERO) == 0)
            return AccountStatus.UNCONFIRMED;

        return new AccountStatus(
                true,
                processValue(token.getBalance()),
                token.getTxHashId());
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {

        CryptoApisTransaction cryptoApisTransaction = cryptoApisClient.getTransactionByHash(COIN, txHash);

        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = cryptoApisTransaction.getGasPrice().multiply(cryptoApisTransaction.getGasUsed());
        BigDecimal receivedValue = cryptoApisTransaction.getValue();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setBuyerAddress(cryptoApisTransaction.getFrom());
        paymentDetails.setTxId(txHash);
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return TetherUtils.convertTetherToUSDT(value);
    }
}
