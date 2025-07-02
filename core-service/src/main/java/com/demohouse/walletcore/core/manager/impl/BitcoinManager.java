package com.demohouse.walletcore.core.manager.impl;

import com.demohouse.walletcore.core.manager.CoinManagerAdapter;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BitcoinManager extends CoinManagerAdapter {

    @Override
    public Coin getCoin() {
        return Coin.BITCOIN;
    }

    @Override
    public List<CryptoCurrencyApiProvider> getPushProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.SO_CHAIN,
                CryptoCurrencyApiProvider.AMBER_DATA,
                CryptoCurrencyApiProvider.BLOCK_CHAIN,
                CryptoCurrencyApiProvider.BLOCK_CYPHER,
                CryptoCurrencyApiProvider.CRYPTO_APIS
        ));
    }

    @Override
    public List<CryptoCurrencyApiProvider> getExplorerProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.SO_CHAIN,
                CryptoCurrencyApiProvider.AMBER_DATA,
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.BTC_EXPLORER,
                CryptoCurrencyApiProvider.TOKEN_VIEW,
                CryptoCurrencyApiProvider.BLOCK_CHAIN,
                CryptoCurrencyApiProvider.BLOCK_CYPHER,
                CryptoCurrencyApiProvider.CRYPTO_APIS
        ));
    }
}
