package com.demohouse.walletcore.core.manager.impl;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.manager.CoinManagerAdapter;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EthereumManager extends CoinManagerAdapter {

    @Override
    public Coin getCoin() {
        return Coin.ETHEREUM;
    }

    @Override
    public List<CryptoCurrencyApiProvider> getPushProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.AMBER_DATA,
                CryptoCurrencyApiProvider.BLOCK_CYPHER,
                CryptoCurrencyApiProvider.CRYPTO_APIS
        ));
    }

    @Override
    public List<CryptoCurrencyApiProvider> getExplorerProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.BLOCK_CYPHER,
                CryptoCurrencyApiProvider.CRYPTO_APIS,
                CryptoCurrencyApiProvider.AMBER_DATA,
                CryptoCurrencyApiProvider.TOKEN_VIEW
                ));
    }
}
