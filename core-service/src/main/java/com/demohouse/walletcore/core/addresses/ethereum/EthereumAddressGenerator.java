package com.demohouse.walletcore.core.addresses.ethereum;

import com.demohouse.walletcore.core.addresses.AddressGenerator;
import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.utils.WalletByteUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECKeyPair;
import com.demohouse.walletcore.utils.signature.ECSignUtils;
import org.bouncycastle.jcajce.provider.digest.Keccak;

import java.nio.charset.StandardCharsets;

public class EthereumAddressGenerator implements AddressGenerator {

    public static void main(String[] args) {
        new EthereumAddressGenerator().generate();
    }

    private String checksumEncode(String address) {
        Keccak.Digest256 digest256 = new Keccak.Digest256();
        byte[] hashBytes = digest256.digest(address.getBytes(StandardCharsets.UTF_8));
        String hash = WalletHexUtils.bytesToHex(hashBytes);

        StringBuilder encoded = new StringBuilder("0x");
        for (int index = 0; index < address.length(); index++) {
            String subHash = hash.substring(index, index + 1);
            String subStr = address.substring(index, index + 1);
            if (Integer.parseInt(subHash, 16) >= 8)
                encoded.append(subStr.toUpperCase());
            else
                encoded.append(subStr.toLowerCase());
        }
        return encoded.toString();
    }

    private String generateEthAddress(ECKeyPair keyPair) {
        Keccak.Digest256 digest256 = new Keccak.Digest256();
        byte[] publicKey = WalletByteUtils.copyOfRange(keyPair.getPublicKey(), 1, keyPair.getPublicKey().length);
        byte[] hashBytes = digest256.digest(publicKey);
        String sha3_256hex = WalletHexUtils.bytesToHex(hashBytes);
        String address = sha3_256hex.substring(sha3_256hex.length() - 40);
        return checksumEncode(address);
    }

    public GeneratedAddress generate() {
        try {
            ECKeyPair keyPair = ECSignUtils.generateECKeyPair();
            String address = generateEthAddress(keyPair);
            System.out.println(keyPair.getPrivateKeyHex());
            System.out.println(address);
            return new GeneratedAddress(keyPair.getPrivateKeyHex(), address);
        } catch (Throwable e) {
            throw new RuntimeException();
        }
    }
}
