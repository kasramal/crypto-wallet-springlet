package com.demohouse.walletcore.core.fees.api.coins;

import com.demohouse.walletcore.core.fees.EstimatedFee;
import com.demohouse.walletcore.core.fees.api.model.EthGasStationPrice;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;

public class EthereumFee implements EstimatedFee {
    BigDecimal fast;
    BigDecimal fastest;
    BigDecimal safeLow;
    BigDecimal average;
    BigDecimal blockNum;
    BigDecimal gasLimit;

    public EthereumFee(EthGasStationPrice gasPrice, BigDecimal gasLimit) {
        this.fast = processValue(gasPrice.getFast());
        this.fastest = processValue(gasPrice.getFastest());
        this.safeLow = processValue(gasPrice.getSafeLow());
        this.average = processValue(gasPrice.getAverage());
        this.blockNum = gasPrice.getBlockNum();
        this.gasLimit = gasLimit;
    }

    public BigDecimal processValue(BigDecimal value) {
        return EthereumUtils.convertWeiToEther(value.multiply(new BigDecimal("100000000")));
    }

    @Override
    public Coin getCoin() {
        return Coin.ETHEREUM;
    }

    @Override
    public BigDecimal getInstantFee() {
        return new BigDecimal("0.000000035").multiply(gasLimit);
    }

    @Override
    public BigDecimal getEvacuationFee() {
        return average.multiply(gasLimit);
    }
}
