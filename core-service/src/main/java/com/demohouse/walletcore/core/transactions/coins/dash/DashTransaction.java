package com.demohouse.walletcore.core.transactions.coins.dash;

import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;

import java.math.BigDecimal;

public class DashTransaction extends GenericTransaction {
    public BigDecimal convertValue(BigDecimal value) {
        return DashUtils.dashToSatoshi(value);
    }
}
