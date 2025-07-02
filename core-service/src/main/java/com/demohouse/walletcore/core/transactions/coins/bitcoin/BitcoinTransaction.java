package com.demohouse.walletcore.core.transactions.coins.bitcoin;

import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;

import java.math.BigDecimal;

public class BitcoinTransaction extends GenericTransaction {
    public BigDecimal convertValue(BigDecimal value) {
        return BitcoinUtils.bitcoinToSatoshi(value);
    }
}
