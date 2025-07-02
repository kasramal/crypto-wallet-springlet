package com.demohouse.walletcore.core.providers.sochain.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SoChainPushTransactionPayloadResponse implements Serializable {

    private String status;
    private SoChainPushTransactionResponse data;
}
