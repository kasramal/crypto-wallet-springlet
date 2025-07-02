package com.demohouse.walletcore.core.providers.cryptoapis.api.models;

import java.math.BigDecimal;

public class CryptoApisAddress {
    private String address;
    private BigDecimal balance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
