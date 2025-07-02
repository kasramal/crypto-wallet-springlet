package com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.tether;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TetherUtils {

    private static final BigDecimal convertFactor = new BigDecimal("1000000");
    private static final BigDecimal zeroByteCost = new BigDecimal("4");
    private static final long nonZeroByteCost = 68;

    public static BigDecimal convertTetherToUSDT(BigDecimal tetherValue) {
        return tetherValue.divide(convertFactor, 6, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertUSDTToTether(BigDecimal usdt) {
        return usdt.multiply(convertFactor);
    }

    public static BigDecimal calculateDataCost(byte[] data) {
        int nonZeroBytes = 0;
        for(byte b : data) {
            if(b != 0) nonZeroBytes++;
        }
        return BigDecimal.valueOf(nonZeroBytes * nonZeroByteCost);
    }
}
