package com.demohouse.walletcore.core.hdwallets;

import com.demohouse.walletcore.entities.Coin;

public class GenericHDWalletGenerator implements HDWalletGenerator {

    @Override
    public HDWalletCredentials generateCredentials(String mnemonic, String password) {
        if (mnemonic == null) {
            mnemonic = MnemonicHelper.generateMnemonic();
        }
        HDWalletCredentials credentials = new HDWalletCredentials();
        credentials.setPassword(password);
        credentials.setMnemonic(mnemonic);
        return credentials;
    }

    @Override
    public HDWallet generate(Coin c, String password) {
        HDWalletCredentials credentials = generateCredentials(null, password);
        return new HDWallet(c, credentials, true);
    }

    @Override
    public HDWallet retrieve(Coin c, String mnemonic, String password) {
        HDWalletCredentials credentials = generateCredentials(mnemonic, password);
        return new HDWallet(c, credentials, true);
    }

    @Override
    public HDWallet retrieve(Coin coin, HDWalletCredentials credentials) {
        return retrieve(coin, credentials.getMnemonic(), credentials.getPassword());
    }

}
