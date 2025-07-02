package com.demohouse.walletcore.core.providers.bitcoincom.api;

public class BitcoinComConstants {

    private static final String BASE_URL = "https://rest.bitcoin.com/v2";

    public static String getAccountBalanceURL(String address) {
        return BASE_URL + "/address/details/" + address;
    }

    public static String getTransactionByAddressURL(String address) {
        return BASE_URL + "/address/transactions/" + address;
    }

    public static String getAccountUTXOsURL(String address) {
        return BASE_URL + "/address/utxo/" + address;
    }

    public static String getTransactionByHashURL(String txHash) {
        return BASE_URL + "/transaction/details/" + txHash;
    }

    public static String getPushTransactionURL(String txHex) {
        return BASE_URL + "/rawtransactions/sendRawTransaction/" + txHex;
    }
}
