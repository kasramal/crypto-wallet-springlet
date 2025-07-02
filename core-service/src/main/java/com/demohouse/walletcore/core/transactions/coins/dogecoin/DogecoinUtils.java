package com.demohouse.walletcore.core.transactions.coins.dogecoin;

import com.demohouse.walletcore.core.addresses.legacy.DogecoinAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DogecoinUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static BigDecimal dogecoinToSatoshi(BigDecimal dogecoin) {
        return dogecoin.multiply(conversionFactor);
    }

    public static BigDecimal satoshiToDogecoin(BigDecimal satoshi) {
        return satoshi.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !new DogecoinAddressGenerator().validateAddress(address);
    }


}
