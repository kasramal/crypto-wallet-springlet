package com.demohouse.walletcore.core.transactions.coins.tezos;

import com.demohouse.walletcore.core.addresses.tezos.TezosAddressGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TezosUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("1000000.0");

    public static BigDecimal xtzToMicroXTZ(BigDecimal xtz) {
        return xtz.multiply(conversionFactor);
    }

    public static BigDecimal microXTZtoXTZ(BigDecimal microXTZ) {
        return microXTZ.divide(conversionFactor, 7, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInValid(String address) {
        return !TezosAddressGenerator.validateAddress(address);
    }

    public static class Prefix {
        public static final byte[] SERIALIZE = new byte[]{3};
        public static final byte[] TZ1 = new byte[]{(byte) 6, (byte) 161, (byte) 159};
        public static final byte[] ED_SK = new byte[]{(byte) 43, (byte) 246, (byte) 78, (byte) 7};
        public static final byte[] ED_PK = new byte[]{(byte) 13, (byte) 15, (byte) 37, (byte) 217};
        public static final byte[] ED_SIG = new byte[]{(byte) 9, (byte) 245, (byte) 205, (byte) 134, (byte) 18};
    }
}
