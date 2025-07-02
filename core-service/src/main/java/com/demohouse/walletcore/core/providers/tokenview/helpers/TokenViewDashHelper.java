package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class TokenViewDashHelper extends TokenViewGenericHelper {

    private static final Coin COIN = Coin.DASH;

    public TokenViewDashHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
