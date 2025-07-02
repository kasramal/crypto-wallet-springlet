package com.demohouse.walletcore.core.providers.btcexplorer;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(6)
public class BtcExplorerService extends CryptoCurrencyApiServiceAdapter {

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_DOES_NOT_SUPPORT_PUSH_TX);
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.BTC_EXPLORER;
    }

}
