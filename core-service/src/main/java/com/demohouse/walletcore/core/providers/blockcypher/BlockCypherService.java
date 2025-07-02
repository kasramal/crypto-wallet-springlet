package com.demohouse.walletcore.core.providers.blockcypher;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiServiceAdapter;
import com.demohouse.walletcore.core.providers.blockcypher.api.BlockCypherClient;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherTransaction;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResultPayload;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(3)
public class BlockCypherService extends CryptoCurrencyApiServiceAdapter {


    private final BlockCypherClient blockCypherClient;

    public BlockCypherService(BlockCypherClient blockCypherClient) {
        this.blockCypherClient = blockCypherClient;
    }

    @Override
    public CryptoCurrencyApiProvider getApiProvider() {
        return CryptoCurrencyApiProvider.BLOCK_CYPHER;
    }

    private TransactionResult calculateResult(String txid) {
        TransactionResult txResult = new TransactionResult();
        TransactionResultPayload payload = new TransactionResultPayload();
        payload.setTxid(txid);
        txResult.setPayload(payload);
        txResult.setProvider(CryptoCurrencyApiProvider.BLOCK_CYPHER);
        return txResult;
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String transactionHex) {
        BlockCypherTransaction tx = blockCypherClient.pushTransaction(coin, transactionHex);
        return calculateResult(tx.getHash());
    }
}
