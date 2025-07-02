package com.demohouse.walletcore.core.transactions.coins.generic;

import java.math.BigDecimal;

public class GenericTransactionOutput {

    private String pubKeyScript;
    private BigDecimal value;

    public String getPubKeyScript() {
        return pubKeyScript;
    }

    public void setPubKeyScript(String pubKeyScript) {
        this.pubKeyScript = pubKeyScript;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
