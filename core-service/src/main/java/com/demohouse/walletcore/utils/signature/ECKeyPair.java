package com.demohouse.walletcore.utils.signature;

import com.demohouse.walletcore.utils.WalletHexUtils;

public class ECKeyPair {
    private final byte[] privateKey;
    private final byte[] publicKey;

    public ECKeyPair(byte[] privateKey, byte[] publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public String getPrivateKeyHex() {
        return WalletHexUtils.bytesToHex(privateKey);
    }

    public String getPublicKeyHex() {
        return WalletHexUtils.bytesToHex(publicKey);
    }

}
