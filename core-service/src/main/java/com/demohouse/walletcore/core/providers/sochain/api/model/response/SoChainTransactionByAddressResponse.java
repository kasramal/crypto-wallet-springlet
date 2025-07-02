package com.demohouse.walletcore.core.providers.sochain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SoChainTransactionByAddressResponse implements Serializable {

    private String network;
    private String address;
    @JsonProperty("txs")
    private List<SoChainTransactionByAddressTransactionResponse> transactions;
}
