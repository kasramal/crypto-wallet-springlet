package com.demohouse.walletcore.core.providers.sochain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SoChainTransactionByAddressTransactionResponse implements Serializable {

    @JsonProperty("txid")
    private String transactionId;
    private String value;
    private Long confirmations;
    @JsonProperty("output_no")
    private Long outputNumber;
    @JsonProperty("script_hex")
    private String script;
}
