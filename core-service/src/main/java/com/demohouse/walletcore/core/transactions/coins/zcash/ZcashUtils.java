package com.demohouse.walletcore.core.transactions.coins.zcash;

import com.demohouse.walletcore.core.addresses.legacy.ZcashAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ZcashUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static BigDecimal zcashToSatoshi(BigDecimal zcash) {
        return zcash.multiply(conversionFactor);
    }

    public static BigDecimal satoshiToZcash(BigDecimal satoshi) {
        return satoshi.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !new ZcashAddressGenerator().validateAddress(address);
    }
}
