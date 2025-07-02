package com.demohouse.walletcore.core.providers.blockchair;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
public class BlockChairService extends CryptoCurrencyApiServiceAdapter {

    private final BlockChairClient blockChairClient;

    public BlockChairService(BlockChairClient blockChairClient) {
        this.blockChairClient = blockChairClient;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        String txHash = blockChairClient.pushTransaction(coin, transactionHex);

        TransactionResult txResult = new TransactionResult();
        TransactionResultPayload payload = new TransactionResultPayload();
        payload.setTxid(txHash);
        txResult.setPayload(payload);
        txResult.setProvider(CryptoCurrencyApiProvider.BLOCK_CHAIR);
        return txResult;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.BLOCK_CHAIR;
    }
}
