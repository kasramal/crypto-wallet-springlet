package com.demohouse.walletcore.utils;

import java.math.BigInteger;

public class WalletHexUtils {

    public static byte[] toBytesPadded(BigInteger value, int length) {
        byte[] result = new byte[length];
        byte[] bytes = value.toByteArray();
        int bytesLength;
        byte srcOffset;
        if (bytes[0] == 0) {
            bytesLength = bytes.length - 1;
            srcOffset = 1;
        } else {
            bytesLength = bytes.length;
            srcOffset = 0;
        }

        if (bytesLength > length) {
            throw new RuntimeException("Input is too large to put in byte array of size " + length);
        } else {
            int destOffset = length - bytesLength;
            System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength);
            return result;
        }
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }

    public static byte[] decodeHexString(String hexString) {
        hexString = hexString.replace("0x", "");
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String longToHex(long value) {
        String hexStr = String.format("%02x", value);
        if (hexStr.length() % 2 == 1) return "0" + hexStr;
        return hexStr;
    }

    public static String longToHex(long value, int length) {
        String str = "0";
        String hexStr = String.format("%02x", value);
        while (hexStr.length() < 2 * length) hexStr = "0" + hexStr;
        return hexStr;
    }

    public static String longToHex(long value, boolean littleEndian) {
        String hexStr = longToHex(value);
        if (littleEndian) return hexStringToLittleEndian(hexStr);
        return hexStr;
    }

    public static String longToHex(long value, int length, boolean littleEndian) {
        String hexStr = longToHex(value, length);
        if (littleEndian) return hexStringToLittleEndian(hexStr);
        return hexStr;
    }

    public static String littleEndianToBigEndian(String hexString) {
        return hexStringToLittleEndian(hexString);
    }

    public static String hexStringToLittleEndian(String hexString) {
        String swappedHecString = "";

        if (hexString.length() % 2 == 1)
            hexString = "0" + hexString;

        for (int i = 0; i < hexString.length(); i += 2) {
            swappedHecString = hexString.charAt(i) + (hexString.charAt(i + 1) + swappedHecString);
        }

        return swappedHecString;
    }
}
