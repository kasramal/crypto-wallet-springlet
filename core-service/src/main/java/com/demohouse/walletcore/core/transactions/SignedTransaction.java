package com.demohouse.walletcore.core.transactions;

import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;

public class SignedTransaction {
    byte[] serializedTx;

    public SignedTransaction(byte[] serializedTx) {
        this.serializedTx = serializedTx;
    }

    public String toHexString() {
        return WalletHexUtils.bytesToHex(serializedTx);
    }

    public String hashSha256() {
        return WalletHashUtils.sha256(serializedTx);
    }

    public String doubleSha256() {
        return WalletHashUtils.doubleSha256(serializedTx);
    }
}
