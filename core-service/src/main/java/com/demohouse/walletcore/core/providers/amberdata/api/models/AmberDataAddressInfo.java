package com.demohouse.walletcore.core.providers.amberdata.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class AmberDataAddressInfo {

    private String value;

    @JsonIgnore
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return new BigDecimal(this.value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
