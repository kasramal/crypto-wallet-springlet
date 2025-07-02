package com.demohouse.walletcore.core.fees.api.model;

public class EarnBitcoinFee {
    Long fastestFee;
    Long halfHourFee;
    Long hourFee;

    public EarnBitcoinFee(Long fastestFee, Long halfHourFee, Long hourFee) {
        this.fastestFee = fastestFee;
        this.halfHourFee = halfHourFee;
        this.hourFee = hourFee;
    }

    public EarnBitcoinFee() { }

    public Long getFastestFee() {
        return fastestFee;
    }

    public void setFastestFee(Long fastestFee) {
        this.fastestFee = fastestFee;
    }

    public Long getHalfHourFee() {
        return halfHourFee;
    }

    public void setHalfHourFee(Long halfHourFee) {
        this.halfHourFee = halfHourFee;
    }

    public Long getHourFee() {
        return hourFee;
    }

    public void setHourFee(Long hourFee) {
        this.hourFee = hourFee;
    }
}
