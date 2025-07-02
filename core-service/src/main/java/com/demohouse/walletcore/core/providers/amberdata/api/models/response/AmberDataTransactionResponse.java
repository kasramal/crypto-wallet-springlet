package com.demohouse.walletcore.core.providers.amberdata.api.models.response;

import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTransaction;

public class AmberDataTransactionResponse {
    private Long status;
    private String title;
    private String description;
    private AmberDataTransaction payload;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AmberDataTransaction getPayload() {
        return payload;
    }

    public void setPayload(AmberDataTransaction payload) {
        this.payload = payload;
    }
}
