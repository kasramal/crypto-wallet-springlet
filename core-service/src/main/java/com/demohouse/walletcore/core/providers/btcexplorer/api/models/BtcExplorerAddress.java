package com.demohouse.walletcore.core.providers.btcexplorer.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BtcExplorerAddress {
    private BigDecimal balance;
    private BigDecimal txCount;
    private BigDecimal unconfirmedTxCount;
    private BigDecimal unconfirmedReceived;
    private BigDecimal unconfirmedSent;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTxCount() {
        return txCount;
    }

    public void setTxCount(BigDecimal txCount) {
        this.txCount = txCount;
    }

    public BigDecimal getUnconfirmedTxCount() {
        return unconfirmedTxCount;
    }

    public void setUnconfirmedTxCount(BigDecimal unconfirmedTxCount) {
        this.unconfirmedTxCount = unconfirmedTxCount;
    }

    public BigDecimal getUnconfirmedReceived() {
        return unconfirmedReceived;
    }

    public void setUnconfirmedReceived(BigDecimal unconfirmedReceived) {
        this.unconfirmedReceived = unconfirmedReceived;
    }

    public BigDecimal getUnconfirmedSent() {
        return unconfirmedSent;
    }

    public void setUnconfirmedSent(BigDecimal unconfirmedSent) {
        this.unconfirmedSent = unconfirmedSent;
    }
}
