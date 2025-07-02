package com.demohouse.walletcore.utils;

public class WalletByteUtils {
    public static byte[] concatenate(byte[]... args) {
        int length = 0;
        for (byte[] arr : args)
            length += arr.length;

        int resPos = 0;
        byte[] res = new byte[length];
        for (byte[] arr : args) {
            System.arraycopy(arr, 0, res, resPos, arr.length);
            resPos += arr.length;
        }
        return res;
    }

    public static byte[] copyOfRange(String hexInput, int start, int end) {
        return copyOfRange(WalletHexUtils.decodeHexString(hexInput), start, end);
    }

    public static byte[] copyOfRange(byte[] input, int start, int end) {
        byte[] result = new byte[end - start];
        System.arraycopy(input, start, result, 0, end - start);
        return result;
    }
}
