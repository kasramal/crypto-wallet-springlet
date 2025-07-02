package com.demohouse.walletcore.core.providers.blockchair.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairTokenContent {

    private List<BlockChairTransaction> transactions;

    private BlockChairAddress address;

    public List<BlockChairTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BlockChairTransaction> transactions) {
        this.transactions = transactions;
    }

    public BlockChairAddress getAddress() {
        return address;
    }

    public void setAddress(BlockChairAddress address) {
        this.address = address;
    }
}
