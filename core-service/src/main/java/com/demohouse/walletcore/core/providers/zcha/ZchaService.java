package com.demohouse.walletcore.core.providers.zcha;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

@Service
public class ZchaService extends CryptoCurrencyApiServiceAdapter {

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.ZCHA;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }
}
