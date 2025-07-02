package com.demohouse.walletcore.core.manager.impl;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.manager.CoinManagerAdapter;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DashManager extends CoinManagerAdapter {

    @Override
    public Coin getCoin() {
        return Coin.DASH;
    }

    @Override
    public List<CryptoCurrencyApiProvider> getPushProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.SO_CHAIN,
                CryptoCurrencyApiProvider.TOKEN_VIEW
                ));
    }

    @Override
    public List<CryptoCurrencyApiProvider> getExplorerProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.SO_CHAIN,
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.TOKEN_VIEW
                ));
    }
}
