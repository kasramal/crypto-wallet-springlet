package com.demohouse.walletcore.core.providers.whatsonchain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WhatsOnChainTransactionInputResponse implements Serializable {

    @JsonProperty("vout")
    private int outputNumber;
    @JsonProperty("txid")
    private String transactionId;
}
