package com.demohouse.walletcore.core.providers;

public enum CryptoCurrencyApiProvider {
    AMBER_DATA("AMBER_DATA"),
    BTC_EXPLORER("BTC_EXPLORER"),
    BLOCK_CYPHER("BLOCK_CYPHER"),
    CRYPTO_APIS("CRYPTO_APIS"),
    BLOCK_CHAIN("BLOCK_CHAIN"),
    BLOCK_CHAIR("BLOCK_CHAIR"),
    TRON_PROVIDER("TRON_PROVIDER"),
    CRYPTO_NOMIC("CRYPTO_NOMIC"),
    SO_CHAIN("SO_CHAIN"),
    TOKEN_VIEW("TOKEN_VIEW"),
    ZCHA("ZCHA"),
    BITCOIN_COM("BITCOIN_COM"),
    WHATS_ON_CHAIN("WHATS_ON_CHAIN");

    private String providerName;

    CryptoCurrencyApiProvider(String providerName) {
        this.providerName = providerName;
    }

    public static CryptoCurrencyApiProvider findByName(String providerName) {
        for (CryptoCurrencyApiProvider c : values()) {
            if (c.getProviderName().equals(providerName)) {
                return c;
            }
        }
        return null;
    }

    public String getProviderName() {
        return providerName;
    }

}
