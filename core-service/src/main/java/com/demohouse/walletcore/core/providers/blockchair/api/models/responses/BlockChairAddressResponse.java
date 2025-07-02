package com.demohouse.walletcore.core.providers.blockchair.api.models.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairAddressContent;

import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairAddressResponse {
    Map<String, BlockChairAddressContent> data;

    public Map<String, BlockChairAddressContent> getData() {
        return data;
    }

    public void setData(Map<String, BlockChairAddressContent> data) {
        this.data = data;
    }
}
