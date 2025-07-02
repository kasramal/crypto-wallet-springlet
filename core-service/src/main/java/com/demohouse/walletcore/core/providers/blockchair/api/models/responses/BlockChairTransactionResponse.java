package com.demohouse.walletcore.core.providers.blockchair.api.models.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTransactionContent;

import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairTransactionResponse {
    Map<String, BlockChairTransactionContent> data;

    public Map<String, BlockChairTransactionContent> getData() {
        return data;
    }

    public void setData(Map<String, BlockChairTransactionContent> data) {
        this.data = data;
    }
}
