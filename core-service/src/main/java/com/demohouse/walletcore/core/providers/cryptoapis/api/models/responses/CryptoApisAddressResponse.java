package com.demohouse.walletcore.core.providers.cryptoapis.api.models.responses;

import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisAddress;

public class CryptoApisAddressResponse {
    private CryptoApisAddress payload;

    public CryptoApisAddress getPayload() {
        return payload;
    }

    public void setPayload(CryptoApisAddress payload) {
        this.payload = payload;
    }
}
