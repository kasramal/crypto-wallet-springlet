package com.demohouse.walletcore.core.providers.tokenview.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenViewTransactionOutput implements Serializable {

    private String address;
    private Long outputNo;
    private BigDecimal value;

    // usage in generate UTXOs
    @JsonIgnore
    private String transactionId;
}
