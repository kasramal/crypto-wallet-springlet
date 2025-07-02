package com.demohouse.walletcore.core.providers.amberdata.api.models.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class AmberDataPushTransactionRequest implements Serializable {

    private String jsonrpc;
    private Long id;
    private String method;
    private List<String> params;
}
