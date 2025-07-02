package com.demohouse.walletcore.core.addresses.eos;

import com.demohouse.walletcore.core.addresses.AddressGenerator;
import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.core.addresses.legacy.LegacyAddressVariant;
import com.demohouse.walletcore.core.addresses.legacy.WifGenerator;
import com.demohouse.walletcore.utils.Base58Utils;
import com.demohouse.walletcore.utils.WalletByteUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECKeyPair;
import com.demohouse.walletcore.utils.signature.ECSignUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EOSAddressGenerator implements AddressGenerator {

    private static final String EOS_ADDRESS_PREFIX = "EOS";
    private final LegacyAddressVariant variant = LegacyAddressVariant.EOS;

    public static byte[] extractHash160FromAddress(String address) {
        byte[] decoded = Base58Utils.decode(address.replace(EOS_ADDRESS_PREFIX, ""));
        byte[] hash160Bytes = new byte[decoded.length - 4];
        System.arraycopy(decoded, 0, hash160Bytes, 0, decoded.length - 4);
        return hash160Bytes;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new EOSAddressGenerator().generate();
    }

    public GeneratedAddress generate() throws NoSuchAlgorithmException {
        ECKeyPair keyPair = ECSignUtils.generateECKeyPair();
        String privKey = WifGenerator.generateWif(keyPair.getPrivateKey(), getVariant());
        byte[] publicKeyBuf = WalletHexUtils.decodeHexString(ECSignUtils.computePublicKey(keyPair.getPrivateKey(), true));
        MessageDigest rmd = MessageDigest.getInstance("RipeMD160");
        byte[] ripe = rmd.digest(publicKeyBuf);
        byte[] checksum = new byte[4];
        System.arraycopy(ripe, 0, checksum, 0, 4);
        byte[] publicKeyData = WalletByteUtils.concatenate(publicKeyBuf, checksum);
        String address = EOS_ADDRESS_PREFIX + Base58Utils.encode(publicKeyData);
        System.out.println(privKey);
        System.out.println(address);
        return new GeneratedAddress(privKey, address);
    }

    public boolean validateAddress(String address) {
        if (!address.startsWith(EOS_ADDRESS_PREFIX)) return false;
        byte[] decodedBytes;

        try {
            decodedBytes = Base58Utils.decode(address.replace(EOS_ADDRESS_PREFIX, ""));
        } catch (Exception e) {
            return false;
        }

        byte[] publicKeyBuf = Arrays.copyOfRange(decodedBytes, 0, decodedBytes.length - 4);
        if (publicKeyBuf.length != 33) return false;

        MessageDigest rmd;
        try {
            rmd = MessageDigest.getInstance("RipeMD160");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] ripe = rmd.digest(publicKeyBuf);
        byte[] checksum = new byte[4];
        System.arraycopy(ripe, 0, checksum, 0, 4);
        for (int i = 0; i < 4; i++) {
            if (decodedBytes[decodedBytes.length - i - 1] != checksum[3 - i]) return false;
        }
        return true;
    }

    LegacyAddressVariant getVariant() {
        return variant;
    }

}
