package com.demohouse.walletcore.core.providers.blockcypher.api;

public class BlockCypherConstants {

    public static String BASE_URL = "https://api.blockcypher.com";

    public static String getAddressFullURL(String address, String BLOCK_CYPHER_VERSION, String BLOCK_CYPHER_COIN, String BLOCK_CYPHER_NETWORK) {
        return BASE_URL + "/" +
                BLOCK_CYPHER_VERSION + "/" +
                BLOCK_CYPHER_COIN + "/" +
                BLOCK_CYPHER_NETWORK + "/"
                + "addrs/"
                + address
                + "/full";
    }

    public static String getTransactionURL(String txHash, String BLOCK_CYPHER_VERSION, String BLOCK_CYPHER_COIN, String BLOCK_CYPHER_NETWORK) {
        return BASE_URL + "/" +
                BLOCK_CYPHER_VERSION + "/" +
                BLOCK_CYPHER_COIN + "/" +
                BLOCK_CYPHER_NETWORK + "/"
                + "txs/"
                + txHash;
    }

    public static String getPushTransactionURL(String BLOCK_CYPHER_VERSION, String BLOCK_CYPHER_COIN, String BLOCK_CYPHER_NETWORK, String BLOCK_CYPHER_TOKEN) {
        return BASE_URL + "/" +
                BLOCK_CYPHER_VERSION + "/" +
                BLOCK_CYPHER_COIN + "/" +
                BLOCK_CYPHER_NETWORK + "/" +
                "/txs/push?token=" +
                BLOCK_CYPHER_TOKEN;
    }
}
