package com.demohouse.walletcore.core.transactions.coins.tron;

import com.demohouse.walletcore.core.transactions.services.CryptoSdkService;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TronUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("1000000.0");

    public static BigDecimal trxToSun(BigDecimal trx) {
        return trx.multiply(conversionFactor);
    }

    public static BigDecimal sunToTrx(BigDecimal sun) {
        return sun.divide(conversionFactor, 7, RoundingMode.HALF_UP);
    }

    public static boolean isAddressValid(String address) {
        return CryptoSdkService.validateAddress(Coin.TRON, address);
    }
}
