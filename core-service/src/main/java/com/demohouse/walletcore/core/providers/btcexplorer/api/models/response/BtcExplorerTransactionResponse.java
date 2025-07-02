package com.demohouse.walletcore.core.providers.btcexplorer.api.models.response;

import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransaction;

public class BtcExplorerTransactionResponse {
    private String status;
    private BtcExplorerTransaction data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BtcExplorerTransaction getData() {
        return data;
    }

    public void setData(BtcExplorerTransaction data) {
        this.data = data;
    }
}
