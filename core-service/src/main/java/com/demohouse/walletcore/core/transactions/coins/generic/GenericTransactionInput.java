package com.demohouse.walletcore.core.transactions.coins.generic;

import java.math.BigDecimal;

public class GenericTransactionInput {

    private String hashLittleEndian;
    private Long index;
    private BigDecimal value;
    private String scriptSig;
    private String pubKeyScript;

    public String getHashLittleEndian() {
        return hashLittleEndian;
    }

    public void setHashLittleEndian(String hashLittleEndian) {
        this.hashLittleEndian = hashLittleEndian;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getScriptSig() {
        return scriptSig;
    }

    public void setScriptSig(String scriptSig) {
        this.scriptSig = scriptSig;
    }

    public String getPubKeyScript() {
        return pubKeyScript;
    }

    public void setPubKeyScript(String pubKeyScript) {
        this.pubKeyScript = pubKeyScript;
    }
}
