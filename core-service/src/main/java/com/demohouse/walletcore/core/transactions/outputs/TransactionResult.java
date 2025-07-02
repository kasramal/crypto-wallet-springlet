package com.demohouse.walletcore.core.transactions.outputs;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;

import java.math.BigDecimal;

public class TransactionResult {

    private TransactionResultPayload payload;
    private BigDecimal fee;
    private CryptoCurrencyApiProvider provider;

    public TransactionResultPayload getPayload() {
        return payload;
    }

    public void setPayload(TransactionResultPayload payload) {
        this.payload = payload;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public CryptoCurrencyApiProvider getProvider() {
        return provider;
    }

    public void setProvider(CryptoCurrencyApiProvider provider) {
        this.provider = provider;
    }
}
