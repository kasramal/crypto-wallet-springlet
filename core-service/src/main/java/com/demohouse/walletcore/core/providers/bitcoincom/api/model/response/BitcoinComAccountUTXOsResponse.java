package com.demohouse.walletcore.core.providers.bitcoincom.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class BitcoinComAccountUTXOsResponse implements Serializable {

    @JsonProperty("legacyAddress")
    private String address;
    @JsonProperty("scriptPubKey")
    private String script;
    @JsonProperty("utxos")
    private List<BitcoinComAccountUTXOsItemResponse> items;

}
