package com.demohouse.walletcore.core.addresses.ripple;

import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.core.addresses.legacy.LegacyAddressGenerator;
import com.demohouse.walletcore.core.addresses.legacy.LegacyAddressVariant;
import com.demohouse.walletcore.core.addresses.legacy.WifGenerator;
import com.demohouse.walletcore.utils.WalletByteUtils;
import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.RippleBase58Utils;
import com.demohouse.walletcore.utils.signature.ECKeyPair;
import com.demohouse.walletcore.utils.signature.ECSignUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class RippleAddressGenerator {

    private final static int SEED_LENGTH = 16;

    private final LegacyAddressVariant variant = LegacyAddressVariant.RIPPLE;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new RippleAddressGenerator().generate();
    }

    public GeneratedAddress generate() throws NoSuchAlgorithmException {
        byte[] seed = new byte[SEED_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(seed);

        ECKeyPair keyPair = ECSignUtils.generateECKeyPair(seed);
        byte[] compressedPubKey = WalletHexUtils.decodeHexString(
                ECSignUtils.computePublicKey(keyPair.getPrivateKey(), true));
        String secret = encodeToBytesChecked(seed, new byte[]{variant.getWifPrefix()});

        byte[] a1 = LegacyAddressGenerator.publicKeyToAddress(compressedPubKey, variant);
        String address = RippleBase58Utils.encode(a1);


        System.out.println(address);
        System.out.println(secret);
        System.out.println(WifGenerator.generateWif(keyPair.getPrivateKey(), LegacyAddressVariant.RIPPLE, true));

        return new GeneratedAddress(secret, address);
    }

    public String encodeToBytesChecked(byte[] input, byte[] version) {
        byte[] buffer = new byte[input.length + version.length];
        System.arraycopy(version, 0, buffer, 0, version.length);
        System.arraycopy(input, 0, buffer, version.length, input.length);
        byte[] checkSum = WalletByteUtils.copyOfRange(WalletHashUtils.doubleSha256(buffer), 0, 4);
        byte[] output = new byte[buffer.length + checkSum.length];
        System.arraycopy(buffer, 0, output, 0, buffer.length);
        System.arraycopy(checkSum, 0, output, buffer.length, checkSum.length);
        return RippleBase58Utils.encode(output);
    }

    public boolean validateAddress(String address) {
        byte[] decodedBytes;

        try {
            decodedBytes = RippleBase58Utils.decode(address);
        } catch (Exception e) {
            return false;
        }

        byte[] hash160Bytes = Arrays.copyOfRange(decodedBytes, 0, decodedBytes.length - 4);
        if (decodedBytes[0] != variant.getVersion()) return false;


        int versionByteLength = 1;

        if (hash160Bytes.length != 20 + versionByteLength) return false;

        byte[] finalEncodedHash = WalletHashUtils.doubleSha256Bytes(hash160Bytes);
        for (int i = 0; i < 4; i++) {
            if (decodedBytes[20 + versionByteLength + i] != finalEncodedHash[i]) return false;
        }

        return true;
    }
}
