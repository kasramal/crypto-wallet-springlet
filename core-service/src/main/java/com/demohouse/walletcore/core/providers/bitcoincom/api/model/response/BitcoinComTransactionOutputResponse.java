package com.demohouse.walletcore.core.providers.bitcoincom.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BitcoinComTransactionOutputResponse implements Serializable {

    @JsonIgnore
    private List<String> addresses;
    private String value;

    @JsonProperty("scriptPubKey")
    private void unPackScriptPubKey(Map<String, Object> scriptPubKey) {
        if (scriptPubKey != null) {
            if (scriptPubKey.containsKey("addresses")) {
                if (scriptPubKey.containsKey("addresses")) {
                    this.addresses = (List<String>) scriptPubKey.get("addresses");
                }
            }
        }
    }
}
