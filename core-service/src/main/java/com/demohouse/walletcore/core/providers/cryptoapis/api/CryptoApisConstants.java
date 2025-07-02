package com.demohouse.walletcore.core.providers.cryptoapis.api;

//@Service
//@Order(2)
public class CryptoApisConstants {

    public static final String MAINNET = "mainnet";
    public static final String HEADER_TOKEN_KEY = "X-API-Key";

    public static String getPushTransactionUrl(String coin) {
        return "https://api.cryptoapis.io/v1/bc/"
                        + coin + "/"
                        + MAINNET
                        + "/txs/" + (coin.equals("eth") || coin.equals("etc") ? "push" : "send");
    }

    public static String getAddressConfirmedTransactionsURL(String coin, String address) {
        return "https://api.cryptoapis.io/v1/bc/"
                + coin + "/"
                + MAINNET
                + "/address/"
                + address
                + "/transactions";
    }

    public static String getAddressUnconfirmedTransactionsURL(String coin, String address) {
        return "https://api.cryptoapis.io/v1/bc/"
                + coin + "/"
                + MAINNET
                + "/address/"
                + address
                + "/unconfirmed-transactions";
    }

    public static String getAddressURL(String coin, String address) {
        return "https://api.cryptoapis.io/v1/bc/"
                + coin + "/"
                + MAINNET
                + "/address/"
                + address;
    }

    public static String getAddressTokenTransferURL(String coin, String address) {
        return "https://api.cryptoapis.io/v1/bc/"
                + coin + "/"
                + MAINNET
                + "/tokens/address/"
                + address
                + "/transfers";
    }

    public static String getAddressTokensURL(String coin, String address) {
        return "https://api.cryptoapis.io/v1/bc/"
                + coin + "/"
                + MAINNET
                + "/tokens/address/"
                + address;
    }

    public static String getTransactionURL(String coin, String txHash) {
        return "https://api.cryptoapis.io/v1/bc/"
                + coin + "/"
                + MAINNET
                + "/txs/basic/txid/"
                + txHash;
    }

    public static String getFullTransactionURL(String coin, String txHash) {
        return "https://api.cryptoapis.io/v1/bc/"
                + coin + "/"
                + MAINNET
                + "/txs/hash/"
                + txHash;
    }
}
