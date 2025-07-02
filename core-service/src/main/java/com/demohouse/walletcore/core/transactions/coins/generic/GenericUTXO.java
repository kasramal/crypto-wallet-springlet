package com.demohouse.walletcore.core.transactions.coins.generic;

import java.math.BigDecimal;
import java.util.Objects;

public class GenericUTXO {

    private String transactionHash;
    private long transactionIndex;
    private String script;
    private String address;
    private BigDecimal value;

    public GenericUTXO(String transactionHash, long transactionIndex, String script, String address, BigDecimal value) {
        this.transactionHash = transactionHash;
        this.transactionIndex = transactionIndex;
        this.script = script;
        this.address = address;
        this.value = value;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public long getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(long transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericUTXO that = (GenericUTXO) o;
        return transactionIndex == that.transactionIndex &&
                Objects.equals(transactionHash, that.transactionHash) &&
                Objects.equals(script, that.script) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionHash, transactionIndex, script, value);
    }
}
