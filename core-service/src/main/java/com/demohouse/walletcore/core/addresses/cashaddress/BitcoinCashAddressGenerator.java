package com.demohouse.walletcore.core.addresses.cashaddress;

import com.demohouse.walletcore.core.addresses.AddressGenerator;
import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.core.addresses.legacy.LegacyAddressVariant;
import com.demohouse.walletcore.core.addresses.legacy.WifGenerator;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.utils.Base58Utils;
import com.demohouse.walletcore.utils.WalletByteUtils;
import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.signature.ECKeyPair;
import com.demohouse.walletcore.utils.signature.ECSignUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

public class BitcoinCashAddressGenerator implements AddressGenerator {

    public static final String MAIN_NET_PREFIX = "bitcoincash";

    private static final LegacyAddressVariant variant = LegacyAddressVariant.BITCOINCASH;

    public static void main(String[] args) {
        new BitcoinCashAddressGenerator().generate();
    }

    public static String ripemd160HashedToP2PkhAddress(byte[] sha256Ripemd160Hashed) {
        byte[] versionBytes = variant.hasTwoVersionBytes()
                ? new byte[]{variant.getVersion(), variant.getSecondVersionByte()}
                : new byte[]{variant.getVersion()};

        byte[] versionPrepended = WalletByteUtils.concatenate(versionBytes, sha256Ripemd160Hashed);
        byte[] doubleHashedToCalculateChecksum = WalletHashUtils.doubleSha256Bytes(versionPrepended);
        return Base58Utils.encode(WalletByteUtils.concatenate(versionPrepended, WalletByteUtils.copyOfRange(doubleHashedToCalculateChecksum, 0, 4)));
    }

    public static byte[] extractHash160FromAddress(String bitcoinCashAddress) {
        if (!validateAddress(bitcoinCashAddress)) {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
        }
        byte[] addressData = BitcoinCashBase32.decode(bitcoinCashAddress);
        addressData = Arrays.copyOfRange(addressData, 0, addressData.length - 8);
        addressData = ArrayConverter.convertBits(addressData, 5, 8, true);
        return Arrays.copyOfRange(addressData, 1, addressData.length);
    }

    public static boolean validateAddress(String bitcoinCashAddress) {
        try {
            if (bitcoinCashAddress == null || bitcoinCashAddress.length() == 0) {
                return false;
            }

            if (!isSingleCase(bitcoinCashAddress))
                return false;
            bitcoinCashAddress = bitcoinCashAddress.toLowerCase();
            byte[] checksumData = WalletByteUtils.concatenate(
                    getPrefixBytes(MAIN_NET_PREFIX),
                    new byte[]{variant.getVersion()},
                    BitcoinCashBase32.decode(bitcoinCashAddress));
            byte[] calculateChecksumBytesPolymod = ChecksumPolymod.calculateChecksumBytesPolymod(checksumData);
            return new BigInteger(calculateChecksumBytesPolymod).compareTo(BigInteger.ZERO) == 0;
        } catch (RuntimeException re) {
            return false;
        }
    }

    private static boolean isSingleCase(String bitcoinCashAddress) {
        return bitcoinCashAddress.equals(bitcoinCashAddress.toLowerCase()) ||
                bitcoinCashAddress.equals(bitcoinCashAddress.toUpperCase());
    }

    private static byte[] getPrefixBytes(String prefixString) {
        byte[] prefixBytes = new byte[prefixString.length()];

        char[] charArray = prefixString.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            prefixBytes[i] = (byte) (charArray[i] & 0x1f);
        }
        return prefixBytes;
    }

    private static String getPrefixString() {
        return MAIN_NET_PREFIX;
    }

    public String createCashAddress(byte[] hash) {
        String prefixString = getPrefixString();
        byte[] prefixBytes = getPrefixBytes(prefixString);
        byte[] payloadBytes = WalletByteUtils.concatenate(new byte[]{variant.getVersion()}, hash);
        payloadBytes = ArrayConverter.convertBits(payloadBytes, 8, 5, false);
        byte[] allChecksumInput = WalletByteUtils.concatenate(
                prefixBytes,
                new byte[]{0},
                payloadBytes,
                new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        byte[] checksumBytes = ChecksumPolymod.calculateChecksumBytesPolymod(allChecksumInput);
        checksumBytes = ArrayConverter.convertBits(checksumBytes, 8, 5, true);
        return BitcoinCashBase32.encode(WalletByteUtils.concatenate(payloadBytes, checksumBytes));
    }

    public GeneratedAddress generate() {
        try {
            ECKeyPair keyPair = ECSignUtils.generateECKeyPair();
            String privKey = WifGenerator.generateWif(keyPair.getPrivateKey(), getVariant());
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] s1 = sha.digest(keyPair.getPublicKey());
            MessageDigest rmd = MessageDigest.getInstance("RipeMD160");
            byte[] r1 = rmd.digest(s1);
            String address = createCashAddress(r1);
            System.out.println(privKey);
            System.out.println(address);
            return new GeneratedAddress(privKey, address);
        } catch (Throwable e) {
            throw new RuntimeException();
        }
    }

    public LegacyAddressVariant getVariant() {
        return variant;
    }
}
