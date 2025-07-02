package com.demohouse.walletcore.core.providers.tokenview.api;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;

public class TokenViewConstants {

    private static final String BASE_URL = "http://www.tokenview.com:8088";
    public static final Long SUCCESS_CODE = 1L;
    public static final String DEFAULT_JSON_RPC = "2.0";
    public static final String DEFAULT_PUSH_ID = "viewtoken";
    public static final String BTC_PUSH_METHOD = "sendrawtransaction";
    public static final String ETH_PUSH_METHOD = "eth_sendRawTransaction";

    public static String getAddressInfoWithoutContractIdURL(Coin coin, String address) {
        return TokenViewConstants.getAddressInfoWithoutContractIdURL(coin, address, 1, 50);
    }

    public static String getPendingTransactionsURL(Coin coin, String address) {
        return BASE_URL + "/pending/ntx/" + TokenViewConstants.getCoinName(coin) + '/' + address;
    }

    public static String getAddressInfoWithoutContractIdURL(Coin coin, String address, int pageNumber, int pageSize) {
        return BASE_URL + "/address/" + TokenViewConstants.getCoinName(coin) + "/" + address + "/" + pageNumber + "/" + pageSize;
    }

    public static String getAddressInfoWithContractIdURL(Coin coin, String address) {
        return BASE_URL + "/" + TokenViewConstants.getCoinName(coin) + "/address/" + address;
    }

    public static String getUTXOsUrl(Coin coin, String address) {
        return BASE_URL + "/unspent/" + TokenViewConstants.getCoinName(coin) + "/" + address + "/1/50";
    }

    public static String getTransactionDetailsURL(Coin coin, String txHash) {
        return BASE_URL + "/tx/" + TokenViewConstants.getCoinName(coin) + "/" + txHash;
    }

    public static String getTokenBalanceURL(Coin coin, String address) {
        return BASE_URL + "/" + TokenViewConstants.getCoinName(coin) + "/address/tokenbalance/" + address;
    }


    public static String getTransactionsOfTokenURL(Coin coin, String address, String tokenAddress) {
        return BASE_URL + "/" + TokenViewConstants.getCoinName(coin) + "/address/tokentrans/" + address + "/" + tokenAddress + "/1/50";
    }

    private static String getCoinName(Coin coin) {
        switch (coin) {
            case BITCOIN:
            case BITCOINCASH:
            case DASH:
            case LITECOIN:
            case DOGECOIN:
            case ETHEREUM:
            case ETHEREUM_CLASSIC:
            case TRON:
                return coin.getIso().toLowerCase();
            case ZCASH:
                return "zcash";
            case BITCOINSV:
                return "bchsv";
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

    public static String getPushURL(Coin coin) {
        return "https://wallet.tokenview.com/onchainwallet/" + getCoinName(coin);
    }
}
