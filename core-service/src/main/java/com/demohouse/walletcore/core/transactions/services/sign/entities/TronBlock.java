package com.demohouse.walletcore.core.transactions.services.sign.entities;

import java.math.BigInteger;

public class TronBlock {
    private BigInteger height;
    private String hash;
    private BigInteger timestamp;

    public BigInteger getHeight() {
        return height;
    }

    public void setHeight(BigInteger height) {
        this.height = height;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
    }
}
