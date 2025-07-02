package com.demohouse.walletcore.core.hdwallets;

import com.demohouse.walletcore.core.thirdparty.crypto.mnemonic;
import com.demohouse.walletcore.entities.Coin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class HDWallet {

    private HDWalletCredentials credentials;
    private BigInteger seed;
    private HDWalletAddress hdWalletAddress;

    public HDWallet(Coin coin, HDWalletCredentials credentials, boolean useSdk) {
        this.credentials = credentials;
        this.seed = mnemonic.seed(credentials.getMnemonic(), credentials.getPassword());
        hdWalletAddress = new HDWalletAddress(coin, seed, useSdk);
    }
}
