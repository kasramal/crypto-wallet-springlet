package com.demohouse.walletcore.core.providers.tronprovider.api.models.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TronGridCreateTransactionRequest implements Serializable {

    private String toAddress;
    private String ownerAddress;
    private long amount;
}
