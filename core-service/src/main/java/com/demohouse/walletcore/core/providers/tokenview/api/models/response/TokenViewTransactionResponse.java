package com.demohouse.walletcore.core.providers.tokenview.api.models.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewTransaction;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenViewTransactionResponse implements Serializable {
    private String msg;
    private Long code;
    private TokenViewTransaction data;
}
