package com.demohouse.walletcore.core.transactions.coins.bitcoinsv;

import com.demohouse.walletcore.core.addresses.legacy.BitcoinAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BitcoinSVUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static BigDecimal bitcoinSVToSatoshi(BigDecimal bitcoinSV) {
        return bitcoinSV.multiply(conversionFactor);
    }

    public static BigDecimal satoshiToBitcoinSV(BigDecimal satoshi) {
        return satoshi.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !new BitcoinAddressGenerator().validateAddress(address);
    }

}
