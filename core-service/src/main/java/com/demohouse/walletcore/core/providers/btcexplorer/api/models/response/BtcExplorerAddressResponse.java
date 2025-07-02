package com.demohouse.walletcore.core.providers.btcexplorer.api.models.response;

import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerAddress;

public class BtcExplorerAddressResponse {
    private String status;
    private BtcExplorerAddress data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BtcExplorerAddress getData() {
        return data;
    }

    public void setData(BtcExplorerAddress data) {
        this.data = data;
    }
}
