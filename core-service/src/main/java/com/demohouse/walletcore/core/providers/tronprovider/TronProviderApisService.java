package com.demohouse.walletcore.core.providers.tronprovider;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.tronprovider.api.TronGridApisClient;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.response.TronGridPushTransactionResponse;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(5)
public class TronProviderApisService extends CryptoCurrencyApiServiceAdapter {

    private final TronGridApisClient tronGridApisClient;

    public TronProviderApisService(TronGridApisClient tronGridApisClient) {
        this.tronGridApisClient = tronGridApisClient;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.TRON_PROVIDER;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        TronGridPushTransactionResponse response = tronGridApisClient.pushTransaction(transactionHex);
        if (response.isResult()) {
            TransactionResult transactionResult = new TransactionResult();
            TransactionResultPayload transactionResultPayload = new TransactionResultPayload();
            transactionResultPayload.setTxid(response.getTxid());
            transactionResult.setPayload(transactionResultPayload);
            transactionResult.setProvider(CryptoCurrencyApiProvider.TRON_PROVIDER);
            return transactionResult;
        } else {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_TRANSACTION);
        }
    }
}
