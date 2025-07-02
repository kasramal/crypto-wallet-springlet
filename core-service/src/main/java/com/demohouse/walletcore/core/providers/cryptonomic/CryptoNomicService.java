package com.demohouse.walletcore.core.providers.cryptonomic;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(6)
public class CryptoNomicService extends CryptoCurrencyApiServiceAdapter {

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.CRYPTO_NOMIC;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        return null;
    }
}
