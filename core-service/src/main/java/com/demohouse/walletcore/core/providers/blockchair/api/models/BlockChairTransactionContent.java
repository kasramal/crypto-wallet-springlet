package com.demohouse.walletcore.core.providers.blockchair.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairTransactionContent {

    private BlockChairTransaction transaction;

    private List<BlockChairTransactionOutput> outputs;

    private List<BlockChairTransactionInput> inputs;

    public BlockChairTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(BlockChairTransaction transaction) {
        this.transaction = transaction;
    }

    public List<BlockChairTransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<BlockChairTransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public List<BlockChairTransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<BlockChairTransactionInput> inputs) {
        this.inputs = inputs;
    }
}
