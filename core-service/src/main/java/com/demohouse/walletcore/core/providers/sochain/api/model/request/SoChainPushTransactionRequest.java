package com.demohouse.walletcore.core.providers.sochain.api.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SoChainPushTransactionRequest implements Serializable {

    @JsonProperty("tx_hex")
    private String transactionHex;
}
