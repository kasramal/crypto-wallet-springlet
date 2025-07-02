package com.demohouse.walletcore.core.transactions.coins.litecoin;

import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;

import java.math.BigDecimal;

public class LitecoinTransaction extends GenericTransaction {
    public BigDecimal convertValue(BigDecimal value) {
        return LitecoinUtils.litecoinToSatoshi(value);
    }
}
