package com.demohouse.walletcore.core.providers.amberdata.api.models;

import java.math.BigDecimal;
import java.util.List;

public class AmberDataAccountTransactions {

    private List<AmberDataTransaction> records;

    private BigDecimal nonce;

    public List<AmberDataTransaction> getRecords() {
        return records;
    }

    public void setRecords(List<AmberDataTransaction> records) {
        this.records = records;
    }

    public BigDecimal getNonce() {
        return nonce;
    }

    public void setNonce(BigDecimal nonce) {
        this.nonce = nonce;
    }
}
