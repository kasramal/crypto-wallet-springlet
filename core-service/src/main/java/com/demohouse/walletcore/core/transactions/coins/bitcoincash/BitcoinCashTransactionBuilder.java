package com.demohouse.walletcore.core.transactions.coins.bitcoincash;

import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;

public class BitcoinCashTransactionBuilder extends GenericTransactionBuilder {

    @Override
    public String generatePublicKeyScript(String address) {
        return BitcoinCashUtils.generatePublicKeyScript(address);
    }
}
