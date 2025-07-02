package com.demohouse.walletcore.core.providers.cryptoapis.api.models.responses;

import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisAddressToken;

import java.util.List;

public class CryptoApisAddressTokensResponse {
    private List<CryptoApisAddressToken> payload;

    public List<CryptoApisAddressToken> getPayload() {
        return payload;
    }

    public void setPayload(List<CryptoApisAddressToken> payload) {
        this.payload = payload;
    }
}
