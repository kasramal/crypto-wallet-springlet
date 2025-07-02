package com.demohouse.walletcore.core.hdwallets;

import com.demohouse.walletcore.core.thirdparty.crypto.hdwallet;
import com.demohouse.walletcore.core.thirdparty.crypto.wallet;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class HDWalletAddress {

    private Coin coin;
    private String xPrivateKey;
    private String xPublicKey;
    private String privateKey;
    private String publicKey;
    private String address;

    public HDWalletAddress(Coin coin, BigInteger seed, boolean useSdk) {
        this.coin = coin;
        if (useSdk) {
            if (coin.getName() == null)
                throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
            String coinName = coin.getName();
            xPrivateKey = hdwallet.xprivatekey_master(seed, coinName, false);
            xPublicKey = hdwallet.xpublickey_from_xprivatekey(xPrivateKey, coinName, false);
            privateKey = hdwallet.privatekey_from_xprivatekey(xPrivateKey, true, coinName, false);
            publicKey = hdwallet.publickey_from_xpublickey(xPublicKey, true, coinName, false);
            address = wallet.address_from_publickey(publicKey, coinName, false);
        }
    }
}
