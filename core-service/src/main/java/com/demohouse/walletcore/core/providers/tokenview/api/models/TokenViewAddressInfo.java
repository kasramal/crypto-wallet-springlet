package com.demohouse.walletcore.core.providers.tokenview.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenViewAddressInfo implements Serializable {
    private String hash;
    private BigDecimal spend;
    private BigDecimal receive;
    private BigDecimal balance;
    private BigDecimal nonce;
    @JsonProperty("txCount")
    private Long transactionCount;
    private List<TokenViewTransaction> txs;
}
