package com.demohouse.walletcore.core.providers.cryptoapis.helpers;

import com.demohouse.walletcore.core.providers.cryptoapis.api.CryptoApisClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class CryptoApisEthereumClassicHelper extends CryptoApisEthereumHelper {

    private static final Coin COIN = Coin.ETHEREUM_CLASSIC;

    public CryptoApisEthereumClassicHelper(CryptoApisClient cryptoApisClient) {
        super(cryptoApisClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
