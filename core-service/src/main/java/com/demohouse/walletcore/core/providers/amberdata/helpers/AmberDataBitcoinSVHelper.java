package com.demohouse.walletcore.core.providers.amberdata.helpers;

import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class AmberDataBitcoinSVHelper extends AmberDataGenericHelper {

    private static final Coin COIN = Coin.BITCOINSV;

    public AmberDataBitcoinSVHelper(AmberDataClient amberDataClient) {
        super(amberDataClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
