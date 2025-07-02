package com.demohouse.walletcore.core.transactions.coins.bitcoincash;

import com.demohouse.walletcore.core.transactions.coins.generic.GenericForkTransaction;

import java.math.BigDecimal;

public class BitcoinCashTransaction extends GenericForkTransaction {
    public BigDecimal convertValue(BigDecimal value) {
        return BitcoinCashUtils.bitcoinCashToSatoshi(value);
    }
}
