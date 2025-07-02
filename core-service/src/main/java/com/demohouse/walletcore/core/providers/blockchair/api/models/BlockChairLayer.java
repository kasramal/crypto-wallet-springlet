package com.demohouse.walletcore.core.providers.blockchair.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairLayer {


    @JsonProperty(value = "erc_20")
    List<BlockChairErc20Token> erc20;

    public List<BlockChairErc20Token> getErc20() {
        return erc20;
    }

    public void setErc20(List<BlockChairErc20Token> erc20) {
        this.erc20 = erc20;
    }
}
