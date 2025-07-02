package com.demohouse.walletcore.core.providers.bitcoincom;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.bitcoincom.api.BitcoinComClient;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

@Service
public class BitcoinComService extends CryptoCurrencyApiServiceAdapter {

    private final BitcoinComClient bitcoinComClient;

    public BitcoinComService(BitcoinComClient bitcoinComClient) {
        this.bitcoinComClient = bitcoinComClient;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.BITCOIN_COM;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        String transactionId = this.bitcoinComClient.pushTransaction(transactionHex);
        TransactionResult transactionResult = new TransactionResult();
        TransactionResultPayload transactionResultPayload = new TransactionResultPayload();
        transactionResultPayload.setTxid(transactionId);
        transactionResult.setPayload(transactionResultPayload);
        transactionResult.setProvider(CryptoCurrencyApiProvider.BITCOIN_COM);
        return transactionResult;
    }
}
