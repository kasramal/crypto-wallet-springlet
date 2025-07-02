package com.demohouse.walletcore.core.fees.api.model;

import java.math.BigDecimal;

public class EthGasStationPrice {
    BigDecimal fast;
    BigDecimal fastest;
    BigDecimal safeLow;
    BigDecimal average;
    BigDecimal blockNum;

    public BigDecimal getFast() {
        return fast;
    }

    public void setFast(BigDecimal fast) {
        this.fast = fast;
    }

    public BigDecimal getFastest() {
        return fastest;
    }

    public void setFastest(BigDecimal fastest) {
        this.fastest = fastest;
    }

    public BigDecimal getSafeLow() {
        return safeLow;
    }

    public void setSafeLow(BigDecimal safeLow) {
        this.safeLow = safeLow;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public BigDecimal getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(BigDecimal blockNum) {
        this.blockNum = blockNum;
    }
}
