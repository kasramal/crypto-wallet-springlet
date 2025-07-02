package com.demohouse.walletcore.core.providers.tronprovider.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TronScanTransactionContract {

    private long amount;
    private String ownerAddress;
    private String toAddress;
}
