package com.demohouse.walletcore.core.manager.impl;

import com.demohouse.walletcore.core.manager.CoinManagerAdapter;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BitcoinCashManager extends CoinManagerAdapter {

    @Override
    public Coin getCoin() {
        return Coin.BITCOINCASH;
    }

    @Override
    public List<CryptoCurrencyApiProvider> getPushProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.BITCOIN_COM,
                CryptoCurrencyApiProvider.CRYPTO_APIS,
                CryptoCurrencyApiProvider.TOKEN_VIEW
        ));
    }

    @Override
    public List<CryptoCurrencyApiProvider> getExplorerProviders() {
        return new ArrayList<>(Arrays.asList(
                CryptoCurrencyApiProvider.BTC_EXPLORER,
                CryptoCurrencyApiProvider.BITCOIN_COM,
                CryptoCurrencyApiProvider.BLOCK_CHAIR,
                CryptoCurrencyApiProvider.CRYPTO_APIS,
                CryptoCurrencyApiProvider.TOKEN_VIEW
        ));
    }
}
