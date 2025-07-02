package com.demohouse.walletcore.core.providers.btcexplorer.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BtcExplorerTransactionInput {
    private List<String> prevAddresses;
    private BigDecimal prevPosition;
    private String prevTxHash;
    private BigDecimal prevValue;
    private String scriptHex;
    private BigDecimal sequence;

    public List<String> getPrevAddresses() {
        return prevAddresses;
    }

    public void setPrevAddresses(List<String> prevAddresses) {
        this.prevAddresses = prevAddresses;
    }

    public BigDecimal getPrevPosition() {
        return prevPosition;
    }

    public void setPrevPosition(BigDecimal prevPosition) {
        this.prevPosition = prevPosition;
    }

    public String getPrevTxHash() {
        return prevTxHash;
    }

    public void setPrevTxHash(String prevTxHash) {
        this.prevTxHash = prevTxHash;
    }

    public BigDecimal getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(BigDecimal prevValue) {
        this.prevValue = prevValue;
    }

    public String getScriptHex() {
        return scriptHex;
    }

    public void setScriptHex(String scriptHex) {
        this.scriptHex = scriptHex;
    }

    public BigDecimal getSequence() {
        return sequence;
    }

    public void setSequence(BigDecimal sequence) {
        this.sequence = sequence;
    }
}
