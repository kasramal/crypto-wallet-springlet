package com.demohouse.walletcore.core.providers.btcexplorer.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BtcExplorerTransactionList {
    private List<BtcExplorerTransaction> list;

    public List<BtcExplorerTransaction> getList() {
        return list;
    }

    public void setList(List<BtcExplorerTransaction> list) {
        this.list = list;
    }
}
