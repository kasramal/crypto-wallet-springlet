package com.demohouse.walletcore.core.transactions.outputs;

import java.math.BigDecimal;

public class PaymentDetails {

    private String txId;
    private String tokenAddress;
    private String buyerAddress;
    private BigDecimal spentValue;
    private BigDecimal receivedValue;
    private BigDecimal fee;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public BigDecimal getSpentValue() {
        return spentValue;
    }

    public void setSpentValue(BigDecimal spentValue) {
        this.spentValue = spentValue;
    }

    public BigDecimal getReceivedValue() {
        return receivedValue;
    }

    public void setReceivedValue(BigDecimal receivedValue) {
        this.receivedValue = receivedValue;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
