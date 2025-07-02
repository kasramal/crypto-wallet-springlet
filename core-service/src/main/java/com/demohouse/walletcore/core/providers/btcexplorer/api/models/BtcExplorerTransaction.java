package com.demohouse.walletcore.core.providers.btcexplorer.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BtcExplorerTransaction {
    private BigDecimal blockHeight;
    private BigDecimal fee;
    private String hash;
    private List<BtcExplorerTransactionInput> inputs;
    private List<BtcExplorerTransactionOutput> outputs;
    private BigDecimal inputsValue;
    private BigDecimal outputsValue;

    public BigDecimal getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(BigDecimal blockHeight) {
        this.blockHeight = blockHeight;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<BtcExplorerTransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<BtcExplorerTransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<BtcExplorerTransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<BtcExplorerTransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public BigDecimal getInputsValue() {
        return inputsValue;
    }

    public void setInputsValue(BigDecimal inputsValue) {
        this.inputsValue = inputsValue;
    }

    public BigDecimal getOutputsValue() {
        return outputsValue;
    }

    public void setOutputsValue(BigDecimal outputsValue) {
        this.outputsValue = outputsValue;
    }
}
