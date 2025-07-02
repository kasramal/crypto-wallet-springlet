package com.demohouse.walletcore.core.providers.amberdata.helpers;

import com.demohouse.walletcore.core.providers.BalanceCalculationMode;
import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataAddressInfo;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataToken;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTransaction;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Address;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Token;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Transaction;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20TransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.tether.TetherUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmberDataTetherHelper extends AmberDataEthereumHelper {

    private static final Coin COIN = Coin.TETHER;

    private final AmberDataClient amberDataClient;

    public AmberDataTetherHelper(AmberDataClient amberDataClient) {
        super(amberDataClient);
        this.amberDataClient = amberDataClient;
    }


    private Erc20Address getAddress(String address) {
        List<AmberDataToken> tokensOfAddress = getTokens(address.toLowerCase());

        AmberDataAddressInfo amberDataAddressInfo = amberDataClient.getAddress(Coin.ETHEREUM, address.toLowerCase());
        Erc20Address erc20Address = new Erc20Address();
        BigDecimal nonce = getNonce(address);
        erc20Address.setBalance(EthereumUtils.convertWeiToEther(amberDataAddressInfo.getBalance()));
        erc20Address.setNonce(nonce == null ? new BigDecimal("0") : nonce);

        List<Erc20Token> tokens = new ArrayList<>();
        for (AmberDataToken tokenItt : tokensOfAddress) {
            Erc20Token tokenToBeAdded = new Erc20Token();
            tokenToBeAdded.setBalance(processValue(tokenItt.getAmount()));
            tokenToBeAdded.setTokenAddress(tokenItt.getAddress());
            tokenToBeAdded.setTokenSymbol(tokenItt.getSymbol());
            tokens.add(tokenToBeAdded);
        }
        erc20Address.setTokens(tokens);

        return erc20Address;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment payment = payments.get(0);
        Erc20Address erc20Address = getAddress(from);
        Erc20Transaction transaction = Erc20TransactionBuilder.build(Coin.TETHER, erc20Address, payment.getTo(), payment.getValue(), fee);
        return "0x" + transaction.sign(privateKey, true);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    private List<AmberDataToken> getTokens(String address) {
        return amberDataClient.getTokensOfAddress(Coin.ETHEREUM, address.toLowerCase());
    }

    private AmberDataToken getAddressTetherToken(List<AmberDataToken> tokens) {
        for (AmberDataToken token : tokens) {
            if (token.getSymbol().equals(COIN.getIso()) && token.getAddress().contains(COIN.getContractId()))
                return token;
        }
        return null;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        List<AmberDataToken> tokens = getTokens(address);
        AmberDataToken token = getAddressTetherToken(tokens);
        return token != null ? processValue(token.getAmount()) : BigDecimal.ZERO;
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<AmberDataToken> tokens = getTokens(address);
        AmberDataToken token = getAddressTetherToken(tokens);
        return token != null;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        AmberDataTransaction transaction = amberDataClient.getTransaction(getCoin(), transactionId);
        return transaction != null && transaction.getConfirmations().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        List<AmberDataToken> tokens = getTokens(address);
        AmberDataToken token = getAddressTetherToken(tokens);
        if (token == null)
            return new AccountStatus(
                    false,
                    null,
                    processValue(BigDecimal.ZERO),
                    "",
                    BalanceCalculationMode.CONFIRMED_BALANCE_CALCULATION);

        return new AccountStatus(
                true,
                null,
                processValue(token.getAmount()),
                token.getAddress(),
                BalanceCalculationMode.CONFIRMED_BALANCE_CALCULATION);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String tokenAddress, String address) {
        throw new RuntimeException();
//        AmberDataAccountTransactions tokenContent = amberDataClient.getTransactionOfAddress(COIN.getBaseCoin(), address.toLowerCase());
//        AmberDataTransaction tokenTransaction = null;
//
//        for (AmberDataTransaction transaction : tokenContent.getRecords()) {
//            if (transaction.getTo().get(0).getAddress().toLowerCase().equals(COIN.getContractId().toLowerCase()))
//                tokenTransaction = transaction;
//        }
//
//        PaymentDetails paymentDetails = new PaymentDetails();
//        BigDecimal fee = tokenTransaction.getFee();
//        BigDecimal receivedValue = tokenTransaction.getValue();
//        paymentDetails.setReceivedValue(processValue(receivedValue));
//        paymentDetails.setSpentValue(processValue(receivedValue));
//        paymentDetails.setFee(EthereumUtils.convertWeiToEther(fee));
//        paymentDetails.setBuyerAddress(tokenTransaction.getFrom().get(0).getAddress());
//        paymentDetails.setTxId(tokenTransaction.getHash());
//        paymentDetails.setTokenAddress(tokenAddress);
//        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return TetherUtils.convertTetherToUSDT(value);
    }
}
