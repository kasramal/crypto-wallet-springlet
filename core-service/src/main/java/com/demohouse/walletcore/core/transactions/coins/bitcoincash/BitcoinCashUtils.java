package com.demohouse.walletcore.core.transactions.coins.bitcoincash;

import com.demohouse.walletcore.core.addresses.cashaddress.BitcoinCashAddressGenerator;
import com.demohouse.walletcore.core.addresses.legacy.BitcoinAddressGenerator;
import com.demohouse.walletcore.utils.WalletHexUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.demohouse.walletcore.core.transactions.coins.generic.GenericUtils.*;

public class BitcoinCashUtils {

    private static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static BigDecimal bitcoinCashToSatoshi(BigDecimal bitcoinCash) {
        return bitcoinCash.multiply(conversionFactor);
    }

    public static BigDecimal satoshiToBitcoinCash(BigDecimal satoshi) {
        return satoshi.divide(conversionFactor, 9, RoundingMode.HALF_UP);
    }

    public static boolean isAddressInvalid(String address) {
        return !BitcoinCashAddressGenerator.validateAddress(address);
    }

    public static String generatePublicKeyScript(String address) {
        final byte[] hash160Bytes = BitcoinCashAddressGenerator.extractHash160FromAddress(address);
        final String hash160 = WalletHexUtils.bytesToHex(hash160Bytes);
        return OP_DUP + OP_HASH160 + WalletHexUtils.longToHex(hash160.length() / 2) + hash160 + OP_EQUALVERIFY + OP_CHECKSIG;
    }

    public static String getP2PkhAddress(String bchAddress) {
        final byte[] hash160Bytes = BitcoinCashAddressGenerator.extractHash160FromAddress(bchAddress);
        return BitcoinCashAddressGenerator.ripemd160HashedToP2PkhAddress(hash160Bytes);
    }

    public static String getBchAddressFromP2pkhAddress(String p2pkhAddress) {
        final byte[] hash160Bytes = BitcoinAddressGenerator.extractHash160FromAddress(p2pkhAddress);
        return new BitcoinCashAddressGenerator().createCashAddress(hash160Bytes);
    }
}
