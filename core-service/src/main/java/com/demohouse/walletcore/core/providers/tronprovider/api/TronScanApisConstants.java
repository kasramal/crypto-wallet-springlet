package com.demohouse.walletcore.core.providers.tronprovider.api;

public class TronScanApisConstants {

    private static final String BASE_URL = "https://apilist.tronscan.org/api";

    public static String getTransactionsURL() {
        return BASE_URL + "/transaction";
    }

    public static String getTransactionInfoURL(){
        return BASE_URL + "/transaction-info";
    }

    public static String getAccountURL() {
        return BASE_URL + "/account";
    }
}
