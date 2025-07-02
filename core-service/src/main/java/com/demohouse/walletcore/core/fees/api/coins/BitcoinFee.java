package com.demohouse.walletcore.core.fees.api.coins;

import com.demohouse.walletcore.core.fees.EstimatedFee;
import com.demohouse.walletcore.core.fees.api.model.EarnBitcoinFee;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;

public class BitcoinFee implements EstimatedFee {
    /**
     * A minimum length transaction is supposed to contain a single input and a single output.
     * An input consists of 146 Bytes in average.
     * An output consists of 33 Bytes in average.
     * A transaction consists of 10 constant byte in addition to input and output data.
     * So that a MIN_TX_LENGTH is calculated such below:
     */
    private static final long MIN_TX_LENGTH = 146 + 33 + 10;
    BigDecimal fastestFee;
    BigDecimal halfHourFee;
    BigDecimal hourFee;

    public BitcoinFee(EarnBitcoinFee fee) {
        this.fastestFee = BitcoinUtils.satoshiToBitcoin(new BigDecimal(fee.getFastestFee() * MIN_TX_LENGTH));
        this.halfHourFee = BitcoinUtils.satoshiToBitcoin(new BigDecimal(fee.getHalfHourFee() * MIN_TX_LENGTH));
        this.hourFee = BitcoinUtils.satoshiToBitcoin(new BigDecimal(fee.getHourFee() * MIN_TX_LENGTH));
    }

    @Override
    public Coin getCoin() {
        return Coin.BITCOIN;
    }

    @Override
    public BigDecimal getInstantFee() {
        return halfHourFee;
    }

    @Override
    public BigDecimal getEvacuationFee() {
        return halfHourFee;
    }
}
