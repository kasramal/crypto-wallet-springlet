package com.demohouse.walletcore.core.providers.cryptoapis.api.models;

import java.math.BigDecimal;

public class CryptoApisAddressNonce {

    private BigDecimal nonce;

    public BigDecimal getNonce() {
        return nonce;
    }

    public void setNonce(BigDecimal nonce) {
        this.nonce = nonce;
    }
}
