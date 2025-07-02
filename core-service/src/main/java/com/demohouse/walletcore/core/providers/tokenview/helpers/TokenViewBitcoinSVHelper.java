package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class TokenViewBitcoinSVHelper extends TokenViewGenericHelper{

    public TokenViewBitcoinSVHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
    }

    @Override
    public Coin getCoin() {
        return Coin.BITCOINSV;
    }
}
