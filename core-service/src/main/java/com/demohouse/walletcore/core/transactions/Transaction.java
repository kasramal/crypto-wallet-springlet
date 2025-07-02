package com.demohouse.walletcore.core.transactions;

public interface Transaction {


    SignedTransaction sign(String secret);

    byte[] serializeUnsignedTransaction();
}
