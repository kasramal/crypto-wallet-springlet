package com.demohouse.walletcore.core.providers.tokenview;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.core.providers.tokenview.api.models.response.TokenViewPushTransactionResponse;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

@Service
public class TokenViewService extends CryptoCurrencyApiServiceAdapter {

    private final TokenViewClient tokenViewClient;

    public TokenViewService(TokenViewClient tokenViewClient) {
        this.tokenViewClient = tokenViewClient;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.TOKEN_VIEW;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        TokenViewPushTransactionResponse pushTransactionResponse = this.tokenViewClient.pushTransaction(coin, transactionHex);
        TransactionResult transactionResult = new TransactionResult();
        TransactionResultPayload transactionResultPayload = new TransactionResultPayload();
        transactionResultPayload.setTxid(pushTransactionResponse.getResult());
        transactionResult.setPayload(transactionResultPayload);
        transactionResult.setProvider(CryptoCurrencyApiProvider.TOKEN_VIEW);
        return transactionResult;
    }
}
