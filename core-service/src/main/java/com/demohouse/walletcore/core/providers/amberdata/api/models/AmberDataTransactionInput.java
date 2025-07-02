package com.demohouse.walletcore.core.providers.amberdata.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class AmberDataTransactionInput {

    private List<String> addresses;
    private Long index;
    private Long sequence;
    private String scriptHex;
    private String transactionHash;
    private BigDecimal value;
    @JsonProperty("spentOutputIndex")
    private Long outputIndex;
    @JsonProperty("spentTransactionHash")
    private String outputTransactionHash;

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getScriptHex() {
        return scriptHex;
    }

    public void setScriptHex(String scriptHex) {
        this.scriptHex = scriptHex;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(Long outputIndex) {
        this.outputIndex = outputIndex;
    }

    public String getOutputTransactionHash() {
        return outputTransactionHash;
    }

    public void setOutputTransactionHash(String outputTransactionHash) {
        this.outputTransactionHash = outputTransactionHash;
    }
}
