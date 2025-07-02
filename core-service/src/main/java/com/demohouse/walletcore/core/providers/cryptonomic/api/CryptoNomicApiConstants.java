package com.demohouse.walletcore.core.providers.cryptonomic.api;

public class CryptoNomicApiConstants {

    public static final String API_KEY_NAME = "apiKey";
    public static final String API_KEY_VALUE = "e9ba203b-a142-4607-a613-eb6164c3ee57";
    private static final String BASE_URL = "https://tezos-prod.cryptonomic-infra.tech:443";

    public static String getGenerateHashURL() {
        return BASE_URL + "/chains/main/blocks/head/hash";
    }

    public static String getCounterURL(String address) {
        return BASE_URL + "/chains/main/blocks/head/context/contracts/" + address + "/counter";
    }

    public static String getBalanceURL(String address) {
        return BASE_URL + "/chains/main/blocks/head/context/contracts/" + address + "/balance";
    }

    public static String getManagerKeyURL(String address) {
        return BASE_URL + "/chains/main/blocks/head/context/contracts/" + address + "/manager_key";
    }

    public static String getForgeOperationURL() {
        return BASE_URL + "/chains/main/blocks/head/helpers/forge/operations";
    }

    public static String getPreApplyOperationURL() {
        return BASE_URL + "/chains/main/blocks/head/helpers/preapply/operations";
    }

    public static String getInjectOperationURL() {
        return BASE_URL + "/injection/operation";
    }
}
