package com.demohouse.walletcore.core.providers.blockchair.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairAddressContent {

    private List<BlockChairUTXO> utxo;

    private BlockChairAddress address;

    private List<BlockChairTransactionCall> calls;

    @JsonProperty(value = "layer_2")
    private BlockChairLayer layer2;

    private List<String> transactions;

    public List<BlockChairUTXO> getUtxo() {
        return utxo;
    }

    public void setUtxo(List<BlockChairUTXO> utxo) {
        this.utxo = utxo;
    }

    public BlockChairAddress getAddress() {
        return address;
    }

    public void setAddress(BlockChairAddress address) {
        this.address = address;
    }

    public List<BlockChairTransactionCall> getCalls() {
        return calls;
    }

    public void setCalls(List<BlockChairTransactionCall> calls) {
        this.calls = calls;
    }

    public BlockChairLayer getLayer2() {
        return layer2;
    }

    public void setLayer2(BlockChairLayer layer2) {
        this.layer2 = layer2;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }
}
