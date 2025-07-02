package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class TokenViewEthereumClassicHelper extends TokenViewEthereumHelper {

    private static final Coin COIN = Coin.ETHEREUM_CLASSIC;

    public TokenViewEthereumClassicHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
