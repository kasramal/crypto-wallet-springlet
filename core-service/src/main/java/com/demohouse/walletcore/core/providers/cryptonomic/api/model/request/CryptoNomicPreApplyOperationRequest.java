package com.demohouse.walletcore.core.providers.cryptonomic.api.model.request;

import com.demohouse.walletcore.core.providers.cryptonomic.api.model.CryptoNomicOperation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class CryptoNomicPreApplyOperationRequest implements Serializable {

    private String branch;
    private Set<CryptoNomicOperation> contents;
    private String protocol;
    private String signature;
}
