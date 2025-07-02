package com.demohouse.walletcore.core.providers.cryptoapis.api.models.responses;

import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisAddressTokenTransfer;

import java.util.List;

public class CryptoApisAddressTokenTransfersResponse {
    private List<CryptoApisAddressTokenTransfer> payload;

    public List<CryptoApisAddressTokenTransfer> getPayload() {
        return payload;
    }

    public void setPayload(List<CryptoApisAddressTokenTransfer> payload) {
        this.payload = payload;
    }
}
