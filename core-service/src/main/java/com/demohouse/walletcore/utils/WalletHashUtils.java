package com.demohouse.walletcore.utils;

import org.bouncycastle.jcajce.provider.digest.Keccak.Digest256;
import org.bouncycastle.jcajce.provider.digest.Keccak.DigestKeccak;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class WalletHashUtils {
    private WalletHashUtils() {
    }

    public static byte[] sha256Ripemd160(byte[] hexInput) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] s1 = sha.digest(hexInput);
            MessageDigest rmd = MessageDigest.getInstance("RipeMD160");
            return rmd.digest(s1);
        } catch (Exception e) {
            return new byte[0];
        }
    }


    public static byte[] doubleSha256Bytes(byte[] hexInput) {
        return WalletHexUtils.decodeHexString(doubleSha256(hexInput));
    }

    public static String doubleSha256(byte[] hexInput) {
        return doubleSha256(WalletHexUtils.bytesToHex(hexInput));
    }

    public static String doubleSha256(String hexInput) {
        try {
            byte[] rawTransaction = WalletHexUtils.decodeHexString(hexInput);
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] finalEncodedHash = sha256.digest(sha256.digest(rawTransaction));
            return WalletHexUtils.bytesToHex(finalEncodedHash);
        } catch (Exception e) {
            return "";
        }
    }

    public static String sha256(String hexInput) {
        return sha256(WalletHexUtils.decodeHexString(hexInput));
    }
    public static String sha256(byte[] inputBytes) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha256.digest(inputBytes);
            return WalletHexUtils.bytesToHex(digest);
        } catch (Exception e) {
            return "";
        }
    }

    public static String sha3(String hexInput) {
        byte[] bytes = Numeric.hexStringToByteArray(hexInput);
        byte[] result = sha3(bytes);
        return Numeric.toHexString(result);
    }

    public static byte[] sha3(byte[] input, int offset, int length) {
        DigestKeccak kecc = new Digest256();
        kecc.update(input, offset, length);
        return kecc.digest();
    }

    public static byte[] sha3(byte[] input) {
        return sha3(input, 0, input.length);
    }

    public static String sha3String(String utf8String) {
        return Numeric.toHexString(sha3(utf8String.getBytes(StandardCharsets.UTF_8)));
    }

}
