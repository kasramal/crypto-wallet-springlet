package com.demohouse.walletcore.core.providers.blockchain;

import info.blockchain.api.APIException;
import info.blockchain.api.pushtx.PushTx;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Order(2)
public class BlockChainService extends CryptoCurrencyApiServiceAdapter {

    private TransactionResult calculateResult(String txData) {
        TransactionResult txResult = new TransactionResult();
        TransactionResultPayload payload = new TransactionResultPayload();
        payload.setTxid(
                GenericTransaction.hash(txData)
        );
        txResult.setPayload(payload);
        txResult.setProvider(CryptoCurrencyApiProvider.BLOCK_CHAIN);
        return txResult;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        try {
            PushTx.pushTx(transactionHex);
        } catch (APIException | IOException e) {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_TRANSACTION);
        }
        return calculateResult(transactionHex);
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.BLOCK_CHAIN;
    }
}
