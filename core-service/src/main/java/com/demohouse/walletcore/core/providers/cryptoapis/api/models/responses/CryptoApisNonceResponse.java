package com.demohouse.walletcore.core.providers.cryptoapis.api.models.responses;

import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisAddressNonce;

public class CryptoApisNonceResponse {
    private CryptoApisAddressNonce payload;

    public CryptoApisAddressNonce getPayload() {
        return payload;
    }

    public void setPayload(CryptoApisAddressNonce payload) {
        this.payload = payload;
    }
}
