package com.demohouse.walletcore.core.providers.tokenview.api.models;

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
public class TokenViewTransaction implements Serializable {
    private String txid;
    private BigDecimal fee;
    private Long confirmations;
    private BigDecimal inputCnt;
    private BigDecimal nonce;
    private List<TokenViewTransactionInput> inputs;
    private List<TokenViewTransactionOutput> outputs;
    private String from;
    private String to;
    private String tokenAddress;
    private String tokenSymbol;
    private BigDecimal value;
    private BigDecimal gasPrice;
    private BigDecimal gasLimit;
}
