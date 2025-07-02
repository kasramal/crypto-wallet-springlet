package com.demohouse.walletcore.core.transactions.coins.dash;

import com.demohouse.walletcore.core.addresses.legacy.DashAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DashUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static BigDecimal dashToSatoshi(BigDecimal dash) {
        return dash.multiply(conversionFactor);
    }

    public static BigDecimal satoshiToDash(BigDecimal satoshi) {
        return satoshi.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !new DashAddressGenerator().validateAddress(address);
    }

}
