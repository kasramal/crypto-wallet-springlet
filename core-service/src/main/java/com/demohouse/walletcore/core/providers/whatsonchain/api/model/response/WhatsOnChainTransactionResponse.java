package com.demohouse.walletcore.core.providers.whatsonchain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
public class WhatsOnChainTransactionResponse implements Serializable {

    @JsonProperty("txid")
    private String transactionId;
    @JsonProperty("vin")
    private Set<WhatsOnChainTransactionInputResponse> inputs;
    @JsonProperty("vout")
    private Set<WhatsOnChainTransactionOutputResponse> outputs;
    private BigInteger confirmations;
}
