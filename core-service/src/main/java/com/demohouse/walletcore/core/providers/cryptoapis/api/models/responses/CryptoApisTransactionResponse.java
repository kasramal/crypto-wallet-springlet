package com.demohouse.walletcore.core.providers.cryptoapis.api.models.responses;

import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransaction;

public class CryptoApisTransactionResponse {
    private CryptoApisTransaction payload;

    public CryptoApisTransaction getPayload() {
        return payload;
    }

    public void setPayload(CryptoApisTransaction payload) {
        this.payload = payload;
    }
}
