package com.demohouse.walletcore.core.transactions;

import java.math.BigDecimal;

public class CryptoCurrencyPayment {
    private String from;
    private String to;
    private BigDecimal value;

    public CryptoCurrencyPayment(String to, BigDecimal value) {
        this.to = to;
        this.value = value;
    }

    public CryptoCurrencyPayment(String from, String to, BigDecimal value) {
        this.from = from;
        this.to = to;
        this.value = value;
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

    public void setTo(String toAddress) {
        this.to = toAddress;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
