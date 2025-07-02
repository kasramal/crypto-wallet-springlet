package com.demohouse.walletcore.core.hdwallets;

import com.demohouse.walletcore.entities.Coin;

public class HDWalletFactory {

    public static HDWalletGenerator getHDWalletGenerator(Coin coin) {
        switch (coin) {
            default:
                return new GenericHDWalletGenerator();
        }
    }

    public static void main(String[] args) {
        Coin coin = Coin.BITCOIN;
        String password = "MY NAME IS KASRA";

        HDWalletGenerator hdWalletGenerator = HDWalletFactory.getHDWalletGenerator(coin);
        HDWallet hdWallet = hdWalletGenerator.generate(coin, password);
        HDWallet hdWallet2 = hdWalletGenerator.retrieve(coin, hdWallet.getCredentials().getMnemonic(), hdWallet.getCredentials().getPassword());

        System.out.println("Generated HDWallet:");
        System.out.println(hdWallet.getHdWalletAddress().getPrivateKey());
        System.out.println(hdWallet.getHdWalletAddress().getAddress());

        System.out.println("Retrieved HDWallet:");
        System.out.println(hdWallet2.getHdWalletAddress().getPrivateKey());
        System.out.println(hdWallet2.getHdWalletAddress().getAddress());
    }

}

