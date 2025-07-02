package com.demohouse.walletcore.core.transactions.coins.bitcoinsv;

import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericForkTransaction;

import java.math.BigDecimal;

public class BitcoinSVTransaction extends GenericForkTransaction {
    public BigDecimal convertValue(BigDecimal value) {
        return BitcoinUtils.bitcoinToSatoshi(value);
    }
}
