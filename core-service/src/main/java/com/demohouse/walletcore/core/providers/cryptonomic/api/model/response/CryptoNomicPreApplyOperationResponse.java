package com.demohouse.walletcore.core.providers.cryptonomic.api.model.response;

import com.demohouse.walletcore.core.providers.cryptonomic.api.model.CryptoNomicOperation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class CryptoNomicPreApplyOperationResponse implements Serializable {

    private Set<CryptoNomicOperation> contents;
    private String signature;
}
