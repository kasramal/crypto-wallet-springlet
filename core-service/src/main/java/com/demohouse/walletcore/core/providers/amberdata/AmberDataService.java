package com.demohouse.walletcore.core.providers.amberdata;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(5)
public class AmberDataService extends CryptoCurrencyApiServiceAdapter {


    private final AmberDataClient amberDataClient;

    public AmberDataService(AmberDataClient amberDataClient) {
        this.amberDataClient = amberDataClient;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        if (coin.isSecondLayer())
            coin = coin.getBaseCoin();

        String txHash = amberDataClient.pushTransaction(coin, transactionHex);

        TransactionResult txResult = new TransactionResult();
        TransactionResultPayload payload = new TransactionResultPayload();
        payload.setTxid(txHash);
        txResult.setPayload(payload);
        txResult.setProvider(CryptoCurrencyApiProvider.AMBER_DATA);

        return txResult;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.AMBER_DATA;
    }

}
