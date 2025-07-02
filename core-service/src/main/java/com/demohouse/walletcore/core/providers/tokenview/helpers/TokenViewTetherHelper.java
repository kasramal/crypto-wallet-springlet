package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.BalanceCalculationMode;
import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewAddressInfo;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewToken;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewTransaction;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumAddress;
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
public class TokenViewTetherHelper extends TokenViewEthereumHelper {

    private static final Coin COIN = Coin.TETHER;

    private final TokenViewClient tokenViewClient;

    public TokenViewTetherHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
        this.tokenViewClient = tokenViewClient;
    }


    private Erc20Address getAddress(String address) {
        List<TokenViewToken> tokensOfAddress = getTokens(address.toLowerCase());
        TokenViewAddressInfo tokenViewAddressInfo = tokenViewClient.getAddressWithContractId(getCoin().getBaseCoin(), address.toLowerCase());
        EthereumAddress ethereumAddress = new EthereumAddress();
        Erc20Address erc20Address = new Erc20Address();

        ethereumAddress.setBalance(tokenViewAddressInfo.getBalance());
        ethereumAddress.setNonce(tokenViewAddressInfo.getNonce().add(countOutgoingPendingTransactions(address)));

        List<Erc20Token> tokens = new ArrayList<>();
        for (TokenViewToken tokenItt : tokensOfAddress) {
            Erc20Token tokenToBeAdded = new Erc20Token();
            tokenToBeAdded.setBalance(TetherUtils.convertUSDTToTether(tokenItt.getBalance()));
            tokenToBeAdded.setTokenAddress(tokenItt.getHash());
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

    private List<TokenViewToken> getTokens(String address) {
        return tokenViewClient.getTokenViewBalance(Coin.ETHEREUM, address.toLowerCase());
    }

    private TokenViewToken getAddressTetherToken(List<TokenViewToken> tokens) {
        for (TokenViewToken token : tokens) {
            if (token.getSymbol().equals(COIN.getIso()) && token.getHash().contains(COIN.getContractId()))
                return token;
        }
        return null;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        List<TokenViewToken> tokens = getTokens(address);
        TokenViewToken token = getAddressTetherToken(tokens);
        return token != null ? processValue(token.getBalance()) : BigDecimal.ZERO;
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<TokenViewToken> tokens = getTokens(address);
        TokenViewToken token = getAddressTetherToken(tokens);
        return token != null;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        List<TokenViewToken> tokens = getTokens(address);
        TokenViewToken token = getAddressTetherToken(tokens);
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
                processValue(token.getBalance()),
                token.getHash(),
                BalanceCalculationMode.CONFIRMED_BALANCE_CALCULATION);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String tokenAddress, String address) {

        List<TokenViewTransaction> transactions = tokenViewClient.getTokenTransactions(COIN.getBaseCoin(), address.toLowerCase(), tokenAddress.toLowerCase());
        TokenViewTransaction tokenTransaction = null;

        for (TokenViewTransaction transaction : transactions) {
            if (transaction.getTokenSymbol().toLowerCase().equals(COIN.getContractId().toLowerCase()))
                tokenTransaction = transaction;
        }

        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = tokenTransaction.getFee();
        BigDecimal receivedValue = tokenTransaction.getValue();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue));
        paymentDetails.setFee(EthereumUtils.convertWeiToEther(fee));
        paymentDetails.setBuyerAddress(tokenTransaction.getFrom());
        paymentDetails.setTxId(tokenTransaction.getTxid());
        paymentDetails.setTokenAddress(tokenAddress);
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return TetherUtils.convertTetherToUSDT(value);
    }
}
