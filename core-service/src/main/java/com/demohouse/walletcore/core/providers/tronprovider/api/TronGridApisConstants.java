package com.demohouse.walletcore.core.providers.tronprovider.api;

public class TronGridApisConstants {

    private static final String BASE_URL = "https://api.trongrid.io/wallet";

    public static String getCreateTransactionURL() {
        return BASE_URL + "/createtransaction";
    }

    public static String getPushTransactionURL() {
        return BASE_URL + "/broadcasthex";
    }
}
