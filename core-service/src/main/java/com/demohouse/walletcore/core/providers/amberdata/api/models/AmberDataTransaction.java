package com.demohouse.walletcore.core.providers.amberdata.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class AmberDataTransaction {

    private BigDecimal confirmations;
    private BigDecimal fee;
    private List<AmberDataAddress> from;
    private String hash;
    private BigDecimal nonce;
    private BigDecimal value;
    private List<AmberDataAddress> to;
    private AmberDataStatusResult statusResult;

    private List<AmberDataTransactionInput> inputs;
    private List<AmberDataTransactionOutput> outputs;

    public BigDecimal getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(BigDecimal confirmations) {
        this.confirmations = confirmations;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public List<AmberDataAddress> getFrom() {
        return from;
    }

    public void setFrom(List<AmberDataAddress> from) {
        this.from = from;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigDecimal getNonce() {
        return nonce;
    }

    public void setNonce(BigDecimal nonce) {
        this.nonce = nonce;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public List<AmberDataAddress> getTo() {
        return to;
    }

    public void setTo(List<AmberDataAddress> to) {
        this.to = to;
    }

    public AmberDataStatusResult getStatusResult() {
        return statusResult;
    }

    public void setStatusResult(AmberDataStatusResult statusResult) {
        this.statusResult = statusResult;
    }

    public List<AmberDataTransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<AmberDataTransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<AmberDataTransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<AmberDataTransactionOutput> outputs) {
        this.outputs = outputs;
    }

    @JsonProperty("from")
    private void unPack(Object from) {
        if (from != null) {
            if (from instanceof LinkedHashMap) {
                this.from = Collections.singletonList(new AmberDataAddress((String) ((LinkedHashMap) from).get("address")));
            } else if (from instanceof ArrayList) {
                LinkedHashMap tmp = (LinkedHashMap) ((ArrayList) from).get(0);
                this.from = Collections.singletonList(new AmberDataAddress((String) tmp.get("address")));
            }
        }
    }
}
