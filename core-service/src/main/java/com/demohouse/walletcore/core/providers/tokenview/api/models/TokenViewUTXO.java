package com.demohouse.walletcore.core.providers.tokenview.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenViewUTXO implements Serializable {
    private String txid;
    private Long outputNo;
    private BigDecimal confirmations;
    private BigDecimal value;
    private String hex;
}
