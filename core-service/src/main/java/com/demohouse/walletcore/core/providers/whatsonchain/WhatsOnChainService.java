package com.demohouse.walletcore.core.providers.whatsonchain;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.whatsonchain.api.WhatsOnChainClient;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

@Service
public class WhatsOnChainService extends CryptoCurrencyApiServiceAdapter {

    private final WhatsOnChainClient whatsOnChainClient;

    public WhatsOnChainService(WhatsOnChainClient whatsOnChainClient) {
        this.whatsOnChainClient = whatsOnChainClient;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.WHATS_ON_CHAIN;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        String transactionId = this.whatsOnChainClient.pushTransaction(transactionHex);
        TransactionResult transactionResult = new TransactionResult();
        TransactionResultPayload transactionResultPayload = new TransactionResultPayload();
        transactionResultPayload.setTxid(transactionId);
        transactionResult.setPayload(transactionResultPayload);
        transactionResult.setProvider(CryptoCurrencyApiProvider.WHATS_ON_CHAIN);
        return transactionResult;
    }
}
