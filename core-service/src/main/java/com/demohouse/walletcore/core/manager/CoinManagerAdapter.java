package com.demohouse.walletcore.core.manager;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiService;
import com.demohouse.walletcore.core.transactions.coins.TransactionValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CoinManagerAdapter implements CoinManager {

    @Value("${wallet.crypto.api.retry.max}")
    private int maxRetries;

    @Autowired
    public TransactionValidatorFactory transactionValidatorFactory;

    @Autowired
    public Set<CryptoCurrencyApiService> cryptoCurrencyApiServices;

    @Override
    public int getMaxRetries() {
        return maxRetries;
    }

    @Override
    public TransactionValidatorFactory getTransactionValidatorFactory() {
        return transactionValidatorFactory;
    }

    @Override
    public List<CryptoCurrencyApiService> getPushServices() {
        return this.getPushProviders()
                .stream()
                .map((this::findCryptoCurrencyApiService))
                .filter(CryptoCurrencyApiService::isAccessible)
                .collect(Collectors.toList());
    }

    @Override
    public List<CryptoCurrencyApiService> getExplorerServices() {
        return this.getExplorerProviders()
                .stream()
                .map((this::findCryptoCurrencyApiService))
                .filter(CryptoCurrencyApiService::isAccessible)
                .collect(Collectors.toList());
    }

    private CryptoCurrencyApiService findCryptoCurrencyApiService(CryptoCurrencyApiProvider cryptoCurrencyApiProvider) {
        final CryptoCurrencyApiService[] service = new CryptoCurrencyApiService[1];
        cryptoCurrencyApiServices.forEach((cryptoCurrencyApiService) -> {
            if (cryptoCurrencyApiService.getApiProvider().equals(cryptoCurrencyApiProvider))
                service[0] = cryptoCurrencyApiService;
        });
        return service[0];
    }
}
