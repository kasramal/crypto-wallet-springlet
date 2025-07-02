package com.demohouse.walletcore.core.providers.amberdata.api;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;

public class AmberDataConstants {

    public static final String HEADER_TOKEN_KEY = "x-api-key";
    public static final String HEADER_BLOCKCHAIN_ID_KEY = "x-amberdata-blockchain-id";
    public static final String BASE_API = "https://web3api.io/api/v2/";

    public static final String DEFAULT_JSON_RPC = "2.0";
    public static final Long DEFAULT_PUSH_ID = 1L;
    public static final String ETH_PUSH_METHOD = "eth_sendRawTransaction";

    public static String getJsonRPCPushMethod(Coin coin) {
        switch (coin) {
            case ETHEREUM:
            case TETHER:
                return "eth_sendRawTransaction";
            case BITCOIN:
                return "sendrawtransaction";
            default:
                throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
        }
    }

    public static String getAddressInfoURL(String address) {
        return BASE_API
                + "addresses/"
                + address
                + "/account-balances/latest";
    }


    public static String getTransactionsOfAddressURL(String address) {
        return BASE_API
                + "addresses/"
                + address
                + "/transactions";
    }

    public static String getPendingTransactionsOfAddressURL(String address) {
        return BASE_API
                + "addresses/"
                + address
                + "/pending-transactions";
    }

    public static String getTokensURL(String address) {
        return BASE_API
                + "addresses/"
                + address
                + "/tokens";
    }

    public static String getTransactionsURL(String txid) {
        return BASE_API
                + "transactions/"
                + txid;
    }

    public static String getPushTransactionURL(String apiKey) {
        return "https://rpc.web3api.io/?x-api-key=" + apiKey;
    }

}
