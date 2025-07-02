package com.demohouse.walletcore.core.providers.sochain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class SoChainTransactionByHashResponse implements Serializable {

    private String network;
    @JsonProperty("txid")
    private String transactionId;
    private Long confirmations;
    private Set<SoChainTransactionByHashUTXOResponse> inputs;
    private Set<SoChainTransactionByHashUTXOResponse> outputs;
}
