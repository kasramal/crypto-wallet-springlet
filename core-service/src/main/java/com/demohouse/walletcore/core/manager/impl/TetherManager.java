package com.demohouse.walletcore.core.manager.impl;

import com.demohouse.walletcore.core.manager.CoinManagerAdapter;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TetherManager extends CoinManagerAdapter {

    @Override
    public Coin getCoin() {
        return Coin.TETHER;
    }

    @Override
    public List<CryptoCurrencyApiProvider> getPushProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.AMBER_DATA
        ));
    }

    @Override
    public List<CryptoCurrencyApiProvider> getExplorerProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.AMBER_DATA,
                CryptoCurrencyApiProvider.TOKEN_VIEW
        ));
    }
}
