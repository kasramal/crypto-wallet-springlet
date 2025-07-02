package com.demohouse.walletcore.core.providers.sochain.api;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;

public class SoChainConstants {

    private static final String BASE_URL = "https://sochain.com/api/v2";

    public static final String SUCCESS_RESPONSE_STATUS = "success";

    public static String getTransactionByHashURL(Coin coin, String txHash) {
        return BASE_URL + "/get_tx/" + SoChainConstants.getCoinName(coin) + "/" + txHash;
    }

    public static String getAddressBalanceURL(Coin coin, String address) {
        return BASE_URL + "/get_address_balance/" + SoChainConstants.getCoinName(coin) + "/" + address;
    }

    public static String getReceivedTransactionByAddressURL(Coin coin, String address) {
        return BASE_URL + "/get_tx_received/" + SoChainConstants.getCoinName(coin) + "/" + address;
    }

    public static String getUnSpentTransactionByAddressURL(Coin coin, String address) {
        return BASE_URL + "/get_tx_unspent/" + SoChainConstants.getCoinName(coin) + "/" + address;
    }

    public static String pushTransactionURL(Coin coin) {
        return BASE_URL + "/send_tx/" + SoChainConstants.getCoinName(coin);
    }

    private static String getCoinName(Coin coin) {
        switch (coin) {
            case BITCOIN:
                return "BTC";
            case DASH:
                return "DASH";
            case DOGECOIN:
                return "DOGE";
            case LITECOIN:
                return "LTC";
            case ZCASH:
                return "ZEC";
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }
}
