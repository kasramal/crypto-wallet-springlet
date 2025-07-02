package com.demohouse.walletcore.core.addresses.legacy;

import com.demohouse.walletcore.core.addresses.AddressGenerator;
import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.utils.Base58Utils;
import com.demohouse.walletcore.utils.WalletByteUtils;
import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.signature.ECKeyPair;
import com.demohouse.walletcore.utils.signature.ECSignUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public abstract class LegacyAddressGenerator implements AddressGenerator {

    public static byte[] publicKeyToAddress(byte[] pubKey, LegacyAddressVariant variant) throws NoSuchAlgorithmException {
        byte[] sha256Ripemd160Hashed = WalletHashUtils.sha256Ripemd160(pubKey);
        byte[] versionBytes = variant.hasTwoVersionBytes()
                ? new byte[]{variant.getVersion(), variant.getSecondVersionByte()}
                : new byte[]{variant.getVersion()};

        byte[] versionPrepended = WalletByteUtils.concatenate(versionBytes, sha256Ripemd160Hashed);
        byte[] doubleHashedToCalculateChecksum = WalletHashUtils.doubleSha256Bytes(versionPrepended);
        return WalletByteUtils.concatenate(versionPrepended, WalletByteUtils.copyOfRange(doubleHashedToCalculateChecksum, 0, 4));
    }

    public static byte[] extractHash160FromAddress(String address) {
        byte[] decodedBytes = Base58Utils.decode(address);
        // Todo: ad-hoc implementation for ZCASH versionBytes
        int versionBytesLength = address.startsWith("t1") || address.startsWith("t2") || address.startsWith("t3") ? 2 : 1;
        return Arrays.copyOfRange(decodedBytes, versionBytesLength, decodedBytes.length - 4);
    }

    public GeneratedAddress generate() throws NoSuchAlgorithmException {
        ECKeyPair keyPair = ECSignUtils.generateECKeyPair();
        String privKey = WifGenerator.generateWif(keyPair.getPrivateKey(), getVariant());
        byte[] a1 = publicKeyToAddress(keyPair.getPublicKey(), getVariant());
        String address = Base58Utils.encode(a1);
        System.out.println(privKey);
        System.out.println(address);
        return new GeneratedAddress(privKey, address);
    }

    public boolean validateAddress(String address) {
        byte[] decodedBytes;

        try {
            decodedBytes = Base58Utils.decode(address);
        } catch (Exception e) {
            return false;
        }

        byte[] hash160Bytes = Arrays.copyOfRange(decodedBytes, 0, decodedBytes.length - 4);
        if (decodedBytes[0] != getVariant().getVersion()) return false;


        int versionByteLength = 1;
        if (getVariant().hasTwoVersionBytes())
            versionByteLength++;

        if (hash160Bytes.length != 20 + versionByteLength) return false;

        byte[] finalEncodedHash = WalletHashUtils.doubleSha256Bytes(hash160Bytes);
        for (int i = 0; i < 4; i++) {
            if (decodedBytes[20 + versionByteLength + i] != finalEncodedHash[i]) return false;
        }

        return true;
    }

    public abstract LegacyAddressVariant getVariant();
}
