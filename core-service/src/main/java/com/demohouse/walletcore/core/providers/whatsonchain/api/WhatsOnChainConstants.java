package com.demohouse.walletcore.core.providers.whatsonchain.api;

public class WhatsOnChainConstants {

    private static final String BASE_URL = "https://api.whatsonchain.com/v1/bsv/main";

    public static String getAccountBalanceURL(String address) {
        return BASE_URL + "/address/" + address + "/balance";
    }

    public static String getTransactionByAddressURL(String address) {
        return BASE_URL + "/address/" + address + "/history";
    }

    public static String getUnSpentTransactionByAddressURL(String address) {
        return BASE_URL + "/address/" + address + "/unspent";
    }

    public static String getTransactionByHashURL(String txHash) {
        return BASE_URL + "/tx/hash/" + txHash;
    }

    public static String pushTransactionURL() {
        return BASE_URL + "/tx/raw";
    }
}
