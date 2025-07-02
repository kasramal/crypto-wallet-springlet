package com.demohouse.walletcore.core.providers.tronprovider.api.models.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class TronGridPushTransactionRequest implements Serializable {

    private String transaction;
}
