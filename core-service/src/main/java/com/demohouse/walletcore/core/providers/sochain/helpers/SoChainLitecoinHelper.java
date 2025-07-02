package com.demohouse.walletcore.core.providers.sochain.helpers;

import com.demohouse.walletcore.core.providers.sochain.api.SoChainClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class SoChainLitecoinHelper extends SoChainGenericHelper {

    public SoChainLitecoinHelper(SoChainClient soChainClient) {
        super(soChainClient);
    }

    @Override
    public Coin getCoin() {
        return Coin.LITECOIN;
    }

}
