package com.demohouse.walletcore.core.providers.cryptoapis;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.cryptoapis.api.CryptoApisClient;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.CryptoApisTransaction;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(4)
public class CryptoApisService extends CryptoCurrencyApiServiceAdapter {

    private final CryptoApisClient cryptoApisClient;

    public CryptoApisService(CryptoApisClient cryptoApisClient) {
        this.cryptoApisClient = cryptoApisClient;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.CRYPTO_APIS;
    }

    private TransactionResult calculateResult(String txid) {
        TransactionResult txResult = new TransactionResult();
        TransactionResultPayload payload = new TransactionResultPayload();
        payload.setTxid(txid);
        txResult.setPayload(payload);
        txResult.setProvider(CryptoCurrencyApiProvider.CRYPTO_APIS);
        return txResult;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        CryptoApisTransaction tx = cryptoApisClient.pushTransaction(coin, transactionHex);
        return calculateResult(tx.getHex());
    }
}
