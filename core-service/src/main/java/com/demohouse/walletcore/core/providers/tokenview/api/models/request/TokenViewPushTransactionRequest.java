package com.demohouse.walletcore.core.providers.tokenview.api.models.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class TokenViewPushTransactionRequest implements Serializable {

    private String jsonrpc;
    private String id;
    private String method;
    private List<String> params;
}
