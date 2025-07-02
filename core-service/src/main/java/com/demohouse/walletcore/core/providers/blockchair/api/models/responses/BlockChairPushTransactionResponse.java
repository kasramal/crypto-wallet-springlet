package com.demohouse.walletcore.core.providers.blockchair.api.models.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTransaction;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairPushTransactionResponse {
    BlockChairTransaction data;

    public BlockChairTransaction getData() {
        return data;
    }

    public void setData(BlockChairTransaction data) {
        this.data = data;
    }
}
