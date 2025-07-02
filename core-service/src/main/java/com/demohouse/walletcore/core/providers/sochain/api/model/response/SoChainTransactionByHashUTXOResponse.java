package com.demohouse.walletcore.core.providers.sochain.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SoChainTransactionByHashUTXOResponse implements Serializable {

    private String value;
    private String address;
}
