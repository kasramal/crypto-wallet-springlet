package com.demohouse.walletcore.core.providers.whatsonchain.api.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WhatsOnChainPushTransactionRequest implements Serializable {

    @JsonProperty("txhex")
    private String transactionHex;
}
