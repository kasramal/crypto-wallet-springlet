package com.demohouse.walletcore.core.transactions.coins.litecoin;

import com.demohouse.walletcore.core.addresses.legacy.LitecoinAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LitecoinUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static BigDecimal litecoinToSatoshi(BigDecimal litecoin) {
        return litecoin.multiply(conversionFactor);
    }

    public static BigDecimal satoshiToLitecoin(BigDecimal satoshi) {
        return satoshi.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !new LitecoinAddressGenerator().validateAddress(address);
    }


}
