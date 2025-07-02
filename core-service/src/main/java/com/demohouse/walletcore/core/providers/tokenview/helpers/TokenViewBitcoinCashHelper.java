package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TokenViewBitcoinCashHelper extends TokenViewGenericHelper {

    private static final Coin COIN = Coin.BITCOINCASH;

    public TokenViewBitcoinCashHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public String generatePublicKeyScript(String address) {
        return BitcoinCashUtils.generatePublicKeyScript(address);
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new BitcoinCashTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }

    public String processAddress(String address) { return BitcoinCashUtils.getP2PkhAddress(address);}
}
