package com.demohouse.walletcore.core.transactions.coins.ripple;

import com.demohouse.walletcore.core.addresses.ripple.RippleAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RippleUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("1000000.0");

    public static BigDecimal rippleToTinyRipple(BigDecimal ripple) {
        return ripple.multiply(conversionFactor);
    }

    public static BigDecimal tinyRippleToRipple(BigDecimal tinyRipple) {
        return tinyRipple.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !new RippleAddressGenerator().validateAddress(address);
    }
}
