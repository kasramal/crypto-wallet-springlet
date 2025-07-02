package com.demohouse.walletcore.core.addresses.tron;

import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.utils.Base58Utils;
import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECKeyPair;
import com.demohouse.walletcore.utils.signature.ECSignUtils;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomTronAddressGenerator {

    private final Logger logger = LoggerFactory.getLogger(CustomTronAddressGenerator.class);

    private static final int PUBLIC_KEY_LENGTH = 64;

    public static void main(String[] args) {
        new CustomTronAddressGenerator().generate();
    }

    private byte[] generatePublicKey(ECKeyPair keyPair) {
        Keccak.Digest256 digest256 = new Keccak.Digest256();
        byte[] publicKey = new byte[PUBLIC_KEY_LENGTH];
        System.arraycopy(keyPair.getPublicKey(), 1, publicKey, 0, PUBLIC_KEY_LENGTH);
       return digest256.digest(publicKey);
    }

    private String generateChecksum(String address) {
        String doubleHash = WalletHashUtils.doubleSha256(address);
        return doubleHash.substring(0, 8);
    }

    private String generateTronAddress(ECKeyPair keyPair) {
        byte[] publicKey = this.generatePublicKey(keyPair);
        String sha3_256PublicKeyHex = WalletHexUtils.bytesToHex(publicKey);
        String extractedAddress = "41" +
                sha3_256PublicKeyHex.substring(sha3_256PublicKeyHex.length() - 40);
        String checksum = this.generateChecksum(extractedAddress);
        String finalAddress = extractedAddress + checksum;
        return Base58Utils.encode(WalletHexUtils.decodeHexString(finalAddress));
    }

    public GeneratedAddress generate() {
        ECKeyPair keyPair = ECSignUtils.generateECKeyPair();
        String address = this.generateTronAddress(keyPair);
        logger.debug(String.format("Tron Address Generator -> PrivateKey: %s || Address: %S", WalletHexUtils.bytesToHex(keyPair.getPrivateKey()), address));
        return new GeneratedAddress(keyPair.getPrivateKeyHex(), address);
    }
}
