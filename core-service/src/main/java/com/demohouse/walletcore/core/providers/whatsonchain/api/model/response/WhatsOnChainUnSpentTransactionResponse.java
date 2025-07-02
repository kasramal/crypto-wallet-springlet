package com.demohouse.walletcore.core.providers.whatsonchain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class WhatsOnChainUnSpentTransactionResponse implements Serializable {

    private Long height;
    @JsonProperty("tx_pos")
    private Long transactionPosition;
    @JsonProperty("tx_hash")
    private String transactionId;
    private BigDecimal value;
}
