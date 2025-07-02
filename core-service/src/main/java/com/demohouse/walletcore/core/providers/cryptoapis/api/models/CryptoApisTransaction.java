package com.demohouse.walletcore.core.providers.cryptoapis.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CryptoApisTransaction {
    private BigDecimal amount;
    private BigDecimal fee;
    private String hash;
    private String txid;
    private String status;
    private String hex;
    private List<CryptoApisTransactionOutput> txouts;
    private List<CryptoApisTransactionInput> txins;
    private Map<String, BigDecimal> sent;
    private Map<String, BigDecimal> received;
    private BigDecimal confirmations;

    private String from;
    private String to;
    private BigDecimal value;
    private BigDecimal gasPrice;
    private BigDecimal gasUsed;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public List<CryptoApisTransactionOutput> getTxouts() {
        return txouts;
    }

    public void setTxouts(List<CryptoApisTransactionOutput> txouts) {
        this.txouts = txouts;
    }

    public List<CryptoApisTransactionInput> getTxins() {
        return txins;
    }

    public void setTxins(List<CryptoApisTransactionInput> txins) {
        this.txins = txins;
    }

    public Map<String, BigDecimal> getSent() {
        return sent;
    }

    public void setSent(Map<String, BigDecimal> sent) {
        this.sent = sent;
    }

    public Map<String, BigDecimal> getReceived() {
        return received;
    }

    public void setReceived(Map<String, BigDecimal> received) {
        this.received = received;
    }

    public BigDecimal getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(BigDecimal confirmations) {
        this.confirmations = confirmations;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigDecimal getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigDecimal gasUsed) {
        this.gasUsed = gasUsed;
    }
}
