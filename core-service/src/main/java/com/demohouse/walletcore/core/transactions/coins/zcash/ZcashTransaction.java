package com.demohouse.walletcore.core.transactions.coins.zcash;

import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;

import java.math.BigDecimal;

public class ZcashTransaction extends GenericTransaction {
    public BigDecimal convertValue(BigDecimal value) {
        return ZcashUtils.zcashToSatoshi(value);
    }
}
