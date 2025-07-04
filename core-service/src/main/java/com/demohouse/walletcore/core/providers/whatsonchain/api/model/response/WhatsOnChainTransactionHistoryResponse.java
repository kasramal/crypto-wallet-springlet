package com.demohouse.walletcore.core.providers.whatsonchain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WhatsOnChainTransactionHistoryResponse implements Serializable {

    @JsonProperty("tx_hash")
    private String transactionHash;
    private Long height;
}
