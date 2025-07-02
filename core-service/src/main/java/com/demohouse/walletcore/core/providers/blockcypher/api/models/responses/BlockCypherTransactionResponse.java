package com.demohouse.walletcore.core.providers.blockcypher.api.models.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherTransaction;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockCypherTransactionResponse {
    private BlockCypherTransaction tx;

    public BlockCypherTransaction getTx() {
        return tx;
    }

    public void setTx(BlockCypherTransaction tx) {
        this.tx = tx;
    }
}
