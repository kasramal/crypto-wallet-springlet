package com.demohouse.walletcore.core.providers.zcha.api;

public class ZchaConstants {

    private static final String BASE_URL = "https://api.zcha.in/v2/mainnet";

    public static String getNetworkURL() {
        return BASE_URL + "/network";
    }

    public static String getAccountBalanceURL(String address) {
        return BASE_URL + "/accounts/" + address;
    }

    public static String getTransactionByHash(String txHash) {
        return BASE_URL + "/transactions/" + txHash;
    }

    public static String getTransactionByAddress(String address) {
        return BASE_URL + "/accounts/" + address + "/recv?limit=5&offset=0&sort=timestamp&direction=descending";
    }
}
