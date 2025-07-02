package com.demohouse.walletcore.core.providers.tronprovider.api.models.enumorate;

public enum TronScanConfirmationStatus {

    CONFIRMED(0),
    UN_CONFIRMED(1),
    ROLL_BACK(2);

    private final int code;

    TronScanConfirmationStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
