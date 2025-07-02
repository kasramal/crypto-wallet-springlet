package com.demohouse.walletcore.core.providers.amberdata.helpers;

import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class AmberDataLitecoinHelper extends AmberDataGenericHelper {

    private static final Coin COIN = Coin.LITECOIN;

    public AmberDataLitecoinHelper(AmberDataClient amberDataClient) {
        super(amberDataClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
