package com.demohouse.walletcore.core.providers.blockchair.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairAddress {

    private BigDecimal balance;

    private BigDecimal nonce;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getNonce() {
        return nonce;
    }

    public void setNonce(BigDecimal nonce) {
        this.nonce = nonce;
    }
}
