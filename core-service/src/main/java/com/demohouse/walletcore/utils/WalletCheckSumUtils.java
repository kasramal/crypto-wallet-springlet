package com.demohouse.walletcore.utils;

public class WalletCheckSumUtils {

    public static byte[] doubleSha256(byte[] input) {
        return WalletCheckSumUtils.doubleSha256(input, 0, 4);
    }

    public static byte[] doubleSha256(byte[] input, int startRange, int endRange) {
        byte[] secretKeyCheckSum = WalletHashUtils.doubleSha256Bytes(input);
        return WalletByteUtils.copyOfRange(secretKeyCheckSum, startRange, endRange);
    }
}
