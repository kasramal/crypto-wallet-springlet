package com.demohouse.walletcore.core.thirdparty.crypto;

import java.math.BigInteger;

public class base32 {

    public static final String digits = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    public static String encode(byte[] b) {
        StringBuilder sb = new StringBuilder();
        BigInteger n = binint.b2n(b);
        BigInteger base = BigInteger.valueOf(digits.length());
        while (n.compareTo(BigInteger.ZERO) > 0) {
            BigInteger r = n.mod(base);
            n = n.divide(base);
            sb.append(digits.charAt(r.intValue()));
        }
        int l = (8 * b.length) / 5 + ((8 * b.length) % 5 > 0 ? 1 : 0);
        assert l >= sb.length();
        while (sb.length() < l) sb.append(digits.charAt(0));
        return sb.reverse().toString();
    }

    public static byte[] decode(String w) {
        BigInteger v = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(digits.length());
        for (int i = 0; i < w.length(); i++) {
            char c = w.charAt(i);
            int index = digits.indexOf(c);
            if (index < 0) throw new IllegalArgumentException("Invalid input");
            v = v.multiply(base).add(BigInteger.valueOf(index));
        }
        byte[] b = binint.n2b(v);
        int l = (5 * w.length()) / 8 + ((5 * w.length()) % 8 > 0 ? 1 : 0);
        assert l >= b.length;
        int zeros = l - b.length;
        if (zeros > 0) {
            byte[] t = new byte[zeros + b.length];
            System.arraycopy(b, 0, t, zeros, b.length);
            b = t;
        }
        return b;
    }

    public static String check_encode(byte[] b) {
        return check_encode(b, new byte[]{ }, new byte[]{ });
    }

    public static String check_encode(byte[] b, byte[] prefix, byte[] suffix) {
        return check_encode(b, prefix, suffix, null);
    }

    public static String check_encode(byte[] b, byte[] prefix, byte[] suffix, hashing.hashfun f) {
        if (f == null) f = base32::_rev_blake2b_5;
        b = bytes.concat(prefix, b, suffix);
        byte[] h = f.hash(b);
        b = bytes.concat(b, h);
        return encode(b);
    }

    public static triple<byte[], byte[], byte[]> check_decode(String w) {
        return check_decode(w, 0, 0);
    }

    public static triple<byte[], byte[], byte[]> check_decode(String w, int prefix_len, int suffix_len) {
        return check_decode(w, prefix_len, suffix_len, 5, null);
    }

    public static triple<byte[], byte[], byte[]> check_decode(String w, int prefix_len, int suffix_len, int hash_len, hashing.hashfun f) {
        if (f == null) f = base32::_rev_blake2b_5;
        byte[] b = decode(w);
        if (b.length < prefix_len + suffix_len + hash_len) throw new IllegalArgumentException("Invalid length");
        if (hash_len == 0) hash_len = -b.length;
        byte[] h = bytes.sub(b, -hash_len);
        b = bytes.sub(b, 0, -hash_len);
        if (!bytes.equ(h, f.hash(b))) {
            byte[] t = bytes.sub(b, 0 ,1);
            b = bytes.sub(b, 1);
            if (!bytes.equ(t, new byte[]{ 0x00 }) || !bytes.equ(h, f.hash(b))) throw new IllegalArgumentException("Invalid hash");
        }
        byte[] prefix = bytes.sub(b, 0, prefix_len);
        b = bytes.sub(b, prefix_len);
        if (suffix_len == 0) suffix_len = -b.length;
        byte[] suffix = bytes.sub(b, -suffix_len);
        b = bytes.sub(b, 0, -suffix_len);
        return new triple<>(b, prefix, suffix);
    }

    public static String translate(String w, String from_digits, String to_digits) {
        if (from_digits == null) from_digits = digits;
        if (to_digits == null) to_digits = digits;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length(); i++) {
            char c = w.charAt(i);
            int index = from_digits.indexOf(c);
            if (index < 0) throw new IllegalArgumentException("Invalid input");
            sb.append(to_digits.charAt(index));
        }
        return sb.toString();
    }

    static byte[] _rev_blake2b_5(byte[] b) {
        return bytes.rev(hashing.blake2b(b, 5));
    }

    static byte[] _rev_crc16(byte[] b) {
        return bytes.rev(crc16.crc16xmodem(b));
    }

}
