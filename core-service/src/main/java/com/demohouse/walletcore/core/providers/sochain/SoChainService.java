package com.demohouse.walletcore.core.providers.sochain;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.sochain.api.SoChainClient;
import com.demohouse.walletcore.core.providers.sochain.api.model.response.SoChainPushTransactionResponse;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

@Service
public class SoChainService extends CryptoCurrencyApiServiceAdapter {

    private final SoChainClient soChainClient;

    public SoChainService(SoChainClient soChainClient) {
        this.soChainClient = soChainClient;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.SO_CHAIN;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        SoChainPushTransactionResponse pushTransactionResponse = this.soChainClient.pushTransaction(coin, transactionHex);
        TransactionResult transactionResult = new TransactionResult();
        TransactionResultPayload transactionResultPayload = new TransactionResultPayload();
        transactionResultPayload.setTxid(pushTransactionResponse.getTransactionId());
        transactionResult.setPayload(transactionResultPayload);
        transactionResult.setProvider(CryptoCurrencyApiProvider.SO_CHAIN);
        return transactionResult;
    }
}
