package com.demohouse.walletcore.core.providers.whatsonchain.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WhatsOnChainTransactionOutputResponse implements Serializable {

    @JsonProperty("n")
    private int number;
    private BigDecimal value;
    @JsonIgnore
    private List<String> addresses;

    @JsonProperty("scriptPubKey")
    private void unPackScriptPubKey(Map<String, Object> scriptPubKey) {
        if (scriptPubKey != null) {
            if (scriptPubKey.containsKey("addresses")) {
                this.addresses = (List<String>) scriptPubKey.get("addresses");
            }
        }
    }
}
