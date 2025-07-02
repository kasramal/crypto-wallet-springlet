package com.demohouse.walletcore.core.providers.blockcypher.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockCypherTransaction {
    private String hash;
    private BigDecimal total;
    private BigDecimal fees;
    private Integer confirmations;
    private List<BlockCypherTransactionInput> inputs;
    private List<BlockCypherTransactionOutput> outputs;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public List<BlockCypherTransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<BlockCypherTransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<BlockCypherTransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<BlockCypherTransactionOutput> outputs) {
        this.outputs = outputs;
    }
}
