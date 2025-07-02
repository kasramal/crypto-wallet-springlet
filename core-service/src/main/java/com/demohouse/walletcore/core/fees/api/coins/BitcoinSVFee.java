package com.demohouse.walletcore.core.fees.api.coins;

import com.demohouse.walletcore.core.fees.EstimatedFee;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;

public class BitcoinSVFee implements EstimatedFee {
    /**
     * A minimum length transaction is supposed to contain a single input and a single output.
     * An input consists of 146 Bytes in average.
     * An output consists of 33 Bytes in average.
     * A transaction consists of 10 constant byte in addition to input and output data.
     * So that a MIN_TX_LENGTH is calculated such below:
     */

    public BitcoinSVFee() {
    }

    @Override
    public Coin getCoin() {
        return Coin.BITCOINSV;
    }

    @Override
    public BigDecimal getInstantFee() {
        return new BigDecimal("0.00008000");
    }

    @Override
    public BigDecimal getEvacuationFee() {
        return new BigDecimal("0.00007000");
    }
}
