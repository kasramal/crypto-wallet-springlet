package com.demohouse.walletcore.core.fees;

import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;

public interface EstimatedFee {
    Coin getCoin();

    BigDecimal getInstantFee();

    BigDecimal getEvacuationFee();
}
