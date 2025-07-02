package com.demohouse.walletcore.core.providers.blockchair.api.models.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTokenContent;

import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairTokenResponse {
    Map<String, BlockChairTokenContent> data;

    public Map<String, BlockChairTokenContent> getData() {
        return data;
    }

    public void setData(Map<String, BlockChairTokenContent> data) {
        this.data = data;
    }
}
