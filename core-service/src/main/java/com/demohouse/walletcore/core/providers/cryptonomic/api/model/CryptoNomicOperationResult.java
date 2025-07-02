package com.demohouse.walletcore.core.providers.cryptonomic.api.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CryptoNomicOperationResult implements Serializable {

    private String status;
}
