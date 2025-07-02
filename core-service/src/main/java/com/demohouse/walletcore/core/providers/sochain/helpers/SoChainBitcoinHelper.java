package com.demohouse.walletcore.core.providers.sochain.helpers;

import com.demohouse.walletcore.core.providers.sochain.api.SoChainClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class SoChainBitcoinHelper extends SoChainGenericHelper {

    public SoChainBitcoinHelper(SoChainClient soChainClient) {
        super(soChainClient);
    }

    @Override
    public Coin getCoin() {
        return Coin.BITCOIN;
    }

}
