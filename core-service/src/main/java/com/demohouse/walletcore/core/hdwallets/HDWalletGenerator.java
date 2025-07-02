package com.demohouse.walletcore.core.hdwallets;

import com.demohouse.walletcore.entities.Coin;

public interface HDWalletGenerator {

    /**
     * This method Generated a pair of mnemonic and password as the credentials for the HDWallet
     *
     * @param mnemonic list of meaningful tokens serialized together
     *                 if mnemonic is null the mnemonic is randomly generated and set in result value (HDWalletCredentials) of the method
     *                 otherwise this method only works as a setter for HDWalletCredentials class
     *                 Sample: "allegiance commitment partners historically precondition emerged angle early concept appropriate contrast unaccustomed here highly administration conventional married whatever"
     * @param password the password string defined by user
     *                 Sample: "My password is not that easy to guess"
     * @return HDWalletCredentials Object containing mnemonic and password
     */
    HDWalletCredentials generateCredentials(String mnemonic, String password);

    /**
     * This method generates a HDWallet using the passed password phrase for the coin
     *
     * @param coin     coin type of the wallet
     * @param password password phrase of the wallet
     * @return HDWallet Object which contains HDWalletCredentials, Seed and HDWallet Address(xPrivateKey, xPublicKey, privateKey, publicKey, address)
     */
    HDWallet generate(Coin coin, String password);

    /**
     * This method is used to retrieve a HDWallet from existing HDWalletCredentials
     *
     * @param coin     coin type of the wallet
     * @param mnemonic list of meaningful tokens serialized together
     * @param password password phrase of the wallet
     * @return
     */
    HDWallet retrieve(Coin coin, String mnemonic, String password);

    /**
     * This method is used to retrieve a HDWallet from existing HDWalletCredentials
     *
     * @param coin     coin type of the wallet
     * @param credentials credentials
     * @return
     */
    HDWallet retrieve(Coin coin, HDWalletCredentials credentials);
}
