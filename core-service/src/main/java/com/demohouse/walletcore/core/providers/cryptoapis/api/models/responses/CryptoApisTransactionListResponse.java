package com.demohouse.walletcore.core.providers.cryptoapis.api.models.responses;

import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransaction;

import java.util.List;

public class CryptoApisTransactionListResponse {
    private List<CryptoApisTransaction> payload;

    public List<CryptoApisTransaction> getPayload() {
        return payload;
    }

    public void setPayload(List<CryptoApisTransaction> payload) {
        this.payload = payload;
    }
}
