package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenViewDogecoinHelper extends TokenViewGenericHelper {

    private static final Coin COIN = Coin.DOGECOIN;

    public TokenViewDogecoinHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
    }

    @Override
    public List<GenericUTXO> getUTXOs(String address) {
        return super.getGeneratedUTXOs(address);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
