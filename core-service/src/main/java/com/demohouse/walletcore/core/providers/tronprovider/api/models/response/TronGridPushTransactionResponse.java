package com.demohouse.walletcore.core.providers.tronprovider.api.models.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TronGridPushTransactionResponse implements Serializable {

    private boolean result;
    private String code;
    private String txid;
    private String message;
    private String transaction;
}
