package com.demohouse.walletcore.core.providers.cryptoapis.helpers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.providers.cryptoapis.api.CryptoApisClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CryptoApisBitcoinCashHelper extends CryptoApisGenericHelper {

    private static final Coin COIN = Coin.BITCOINCASH;

    public CryptoApisBitcoinCashHelper(CryptoApisClient cryptoApisClient) {
        super(cryptoApisClient);
    }

    @Override
    public String preProcessAddress(String address) {
        return  "bitcoincash:" + address ;
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new BitcoinCashTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }
}
