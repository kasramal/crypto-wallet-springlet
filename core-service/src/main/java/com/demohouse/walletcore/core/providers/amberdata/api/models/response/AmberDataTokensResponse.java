package com.demohouse.walletcore.core.providers.amberdata.api.models.response;

import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTokens;

public class AmberDataTokensResponse {
    private Long status;
    private String title;
    private String description;
    private AmberDataTokens payload;

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

    public AmberDataTokens getPayload() {
        return payload;
    }

    public void setPayload(AmberDataTokens payload) {
        this.payload = payload;
    }
}
