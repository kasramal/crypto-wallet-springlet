package com.demohouse.walletcore.core.providers.amberdata.api.models.response;

import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataAddressInfo;

public class AmberDataAddressResponse {
    private Long status;
    private String title;
    private String description;
    private AmberDataAddressInfo payload;

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

    public AmberDataAddressInfo getPayload() {
        return payload;
    }

    public void setPayload(AmberDataAddressInfo payload) {
        this.payload = payload;
    }
}
