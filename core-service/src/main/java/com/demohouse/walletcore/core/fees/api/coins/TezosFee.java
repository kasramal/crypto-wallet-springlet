package com.demohouse.walletcore.core.fees.api.coins;

import com.demohouse.walletcore.core.fees.EstimatedFee;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;

public class TezosFee implements EstimatedFee {
    @Override
    public Coin getCoin() {
        return Coin.TEZOS;
    }

    @Override
    public BigDecimal getInstantFee() {
        return new BigDecimal("0.001350");
    }

    @Override
    public BigDecimal getEvacuationFee() {
        return new BigDecimal("0.001350");
    }
}
