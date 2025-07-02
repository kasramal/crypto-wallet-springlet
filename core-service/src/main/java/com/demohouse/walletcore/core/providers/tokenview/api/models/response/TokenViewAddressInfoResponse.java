package com.demohouse.walletcore.core.providers.tokenview.api.models.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewAddressInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenViewAddressInfoResponse implements Serializable {
    private String msg;
    private Long code;
    private List<TokenViewAddressInfo> data;
}
