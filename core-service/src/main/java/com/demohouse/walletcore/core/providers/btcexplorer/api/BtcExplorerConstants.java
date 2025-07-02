package com.demohouse.walletcore.core.providers.btcexplorer.api;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;

public class BtcExplorerConstants {

    public static final String BTC_BASE_API = "https://chain.api.btc.com/v3/";
    public static final String BCH_BASE_API = "https://bch-chain.api.btc.com/v3/";


    private static String getBaseApi(Coin coin) {
        switch (coin) {
            case BITCOIN: return BTC_BASE_API;
            case BITCOINCASH: return BCH_BASE_API;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

    public static String getAddressInfoURL(Coin coin, String address) {

        return getBaseApi(coin)
                + "address/"
                + address;
    }


    public static String getTransactionURL(Coin coin, String txHash) {
        return getBaseApi(coin)
                + "tx/"
                + txHash;
    }

    public static String getTransactionsOfAddressURL(Coin coin, String address) {
        return getBaseApi(coin)
                + "address/"
                + address
                + "/tx";
    }

}
