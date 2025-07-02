package com.demohouse.walletcore.core.fees.api.coins;

import com.demohouse.walletcore.core.fees.EstimatedFee;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;

public class EthereumClassicFee implements EstimatedFee {
    BigDecimal fast;
    BigDecimal fastest;
    BigDecimal safeLow;
    BigDecimal average;
    BigDecimal blockNum;
    BigDecimal gasLimit;

    public EthereumClassicFee(BigDecimal gasLimit) {
        this.average = new BigDecimal("0.000000001");
        this.gasLimit = gasLimit;
    }

    public BigDecimal processValue(BigDecimal value) {
        return EthereumUtils.convertWeiToEther(value.multiply(new BigDecimal("100000000")));
    }

    @Override
    public Coin getCoin() {
        return Coin.ETHEREUM_CLASSIC;
    }

    @Override
    public BigDecimal getInstantFee() {
        return average.multiply(gasLimit);
    }

    @Override
    public BigDecimal getEvacuationFee() {
        return average.multiply(gasLimit);
    }
}
