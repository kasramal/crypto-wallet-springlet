package com.demohouse.walletcore.core.providers.sochain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SoChainPushTransactionResponse implements Serializable {

    private String network;
    @JsonProperty("txid")
    private String transactionId;
}
