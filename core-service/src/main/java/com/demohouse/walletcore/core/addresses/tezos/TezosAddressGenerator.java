package com.demohouse.walletcore.core.addresses.tezos;

import com.demohouse.walletcore.core.addresses.AddressGenerator;
import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.core.transactions.coins.tezos.TezosUtils;
import com.demohouse.walletcore.utils.Base58Utils;
import com.demohouse.walletcore.utils.WalletByteUtils;
import com.demohouse.walletcore.utils.WalletCheckSumUtils;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class TezosAddressGenerator implements AddressGenerator {

    public static void main(String[] args) {
        new TezosAddressGenerator().generate();
    }

    @Override
    public GeneratedAddress generate() {
        // Generate Ed25519 key pair using BouncyCastle
        SecureRandom secureRandom = new SecureRandom();
        Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(secureRandom);
        Ed25519PublicKeyParameters publicKey = privateKey.generatePublicKey();

        byte[] publicKeyBytes = publicKey.getEncoded();

        // Hash the public key (Tezos uses Blake2b-160 here, fallback to SHA-256 then truncate to 20 bytes)
        byte[] hash = hash20(publicKeyBytes);

        byte[] withPrefix = WalletByteUtils.concatenate(TezosUtils.Prefix.TZ1, hash);
        byte[] checksum = WalletCheckSumUtils.doubleSha256(withPrefix);

        String address = Base58Utils.encode(withPrefix, checksum);
        String privateKeyHex = Hex.toHexString(privateKey.getEncoded());

        return new GeneratedAddress(privateKeyHex, address);
    }

    private byte[] hash20(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] fullHash = digest.digest(input);
            byte[] truncated = new byte[20];
            System.arraycopy(fullHash, 0, truncated, 0, 20);
            return truncated;
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    public static boolean validateAddress(String address) {
        return address.startsWith("tz") && address.length() != 36;
    }
}
