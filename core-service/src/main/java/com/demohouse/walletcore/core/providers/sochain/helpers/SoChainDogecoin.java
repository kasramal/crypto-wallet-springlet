package com.demohouse.walletcore.core.providers.sochain.helpers;

import com.demohouse.walletcore.core.providers.sochain.api.SoChainClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class SoChainDogecoin extends SoChainGenericHelper {

    public SoChainDogecoin(SoChainClient soChainClient) {
        super(soChainClient);
    }

    @Override
    public Coin getCoin() {
        return Coin.DOGECOIN;
    }

}
