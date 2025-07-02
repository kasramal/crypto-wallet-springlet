package com.demohouse.walletcore.core.providers.blockchair.api;

public class BlockChairConstants {

    public static final int UNCONFIRMED_BLOCK_ID = -1;
    public static final String PUSH_TRANSACTION_DATA_KEY = "data";
    private static final String BASE_URL = "https://api.blockchair.com";

    public static String getDashboardAddressUrl(String coin, String address, String apiKey) {
        return BASE_URL + "/" +
                coin +
                "/dashboards/address/" +
                address + (coin.equals("ethereum") ? "?nonce=true&key=" + apiKey : "?key=" + apiKey);
    }

    public static String getDashboardTransactionUrl(String coin, String txId, String apiKey) {
        return BASE_URL + "/" +
                coin +
                "/dashboards/transaction/" +
                txId + "?key=" + apiKey;
    }

    public static String getErc20TokenHolderUri(String tokenAddress, String address, String apiKey) {
        return BASE_URL +
                "/ethereum/erc-20/" +
                tokenAddress + "/" +
                "dashboards/address/" +
                address + "?key=" + apiKey;
    }

    public static String getPushTransactionUrl(String coin, String apiKey) {
        return BASE_URL + "/" +
                coin +
                "/push/transaction" + "?key=" + apiKey;
    }

}
