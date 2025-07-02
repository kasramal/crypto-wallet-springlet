package com.demohouse.walletcore.core.providers.btcexplorer.api.models.response;

import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransactionList;

public class BtcExplorerTransactionsOfAddressResponse {
    private String status;
    private BtcExplorerTransactionList data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BtcExplorerTransactionList getData() {
        return data;
    }

    public void setData(BtcExplorerTransactionList data) {
        this.data = data;
    }
}
