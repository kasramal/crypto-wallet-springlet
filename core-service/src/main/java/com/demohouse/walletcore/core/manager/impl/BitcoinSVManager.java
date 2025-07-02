package com.demohouse.walletcore.core.manager.impl;

import com.demohouse.walletcore.core.manager.CoinManagerAdapter;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BitcoinSVManager extends CoinManagerAdapter {

    @Override
    public Coin getCoin() {
        return Coin.BITCOINSV;
    }

    @Override
    public List<CryptoCurrencyApiProvider> getPushProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.WHATS_ON_CHAIN,
                CryptoCurrencyApiProvider.TOKEN_VIEW
        ));
    }

    @Override
    public List<CryptoCurrencyApiProvider> getExplorerProviders() {
        return new ArrayList<>(Arrays.asList(

                CryptoCurrencyApiProvider.WHATS_ON_CHAIN,
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.TOKEN_VIEW
        ));
    }
}
