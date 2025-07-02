package com.demohouse.walletcore.core.providers.cryptonomic.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CryptoNomicOperationMetaData implements Serializable {

    private CryptoNomicOperationResult operationResult;
}
