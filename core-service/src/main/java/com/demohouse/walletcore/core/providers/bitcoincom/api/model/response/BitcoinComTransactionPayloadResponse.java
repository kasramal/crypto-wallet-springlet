package com.demohouse.walletcore.core.providers.bitcoincom.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class BitcoinComTransactionPayloadResponse implements Serializable {

    @JsonProperty("legacyAddress")
    private String address;
    @JsonProperty("txs")
    private List<BitcoinComTransactionResponse> transactions;
}
