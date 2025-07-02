package com.demohouse.walletcore.core.manager;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoinManagerFactory {

    private final Map<Coin, CoinManager> cryptoCurrencyManagerMap = new HashMap<>();

    public CoinManagerFactory(List<CoinManager> coinManagers) {

        for (CoinManager coinManager : coinManagers) {
            this.cryptoCurrencyManagerMap.put(coinManager.getCoin(), coinManager);
        }
    }

    public CoinManager getManager(Coin coin) {
        CoinManager result = this.cryptoCurrencyManagerMap.get(coin);
        if (result != null)
            return result;
        throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

}
