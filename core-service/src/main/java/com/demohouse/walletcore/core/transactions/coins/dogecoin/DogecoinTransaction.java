package com.demohouse.walletcore.core.transactions.coins.dogecoin;

import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.litecoin.LitecoinUtils;

import java.math.BigDecimal;

public class DogecoinTransaction extends GenericTransaction {
    public BigDecimal convertValue(BigDecimal value) {
        return LitecoinUtils.litecoinToSatoshi(value);
    }
}
