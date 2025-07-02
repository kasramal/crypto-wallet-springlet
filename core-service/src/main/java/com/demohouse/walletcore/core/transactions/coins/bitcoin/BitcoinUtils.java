package com.demohouse.walletcore.core.transactions.coins.bitcoin;

import com.demohouse.walletcore.core.addresses.legacy.BitcoinAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BitcoinUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static BigDecimal bitcoinToSatoshi(BigDecimal bitcoin) {
        return bitcoin.multiply(conversionFactor);
    }

    public static BigDecimal satoshiToBitcoin(BigDecimal satoshi) {
        return satoshi.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !new BitcoinAddressGenerator().validateAddress(address);
    }

}
