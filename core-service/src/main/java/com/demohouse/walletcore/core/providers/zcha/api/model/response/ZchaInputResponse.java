package com.demohouse.walletcore.core.providers.zcha.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ZchaInputResponse implements Serializable {

    private List<String> addresses = new ArrayList<>();
    private BigDecimal value;

    @JsonProperty("retrievedVout")
    private void unPackRetrievedVout(Map<String, Object> retrievedVout) {
        if (retrievedVout != null) {
            if (retrievedVout.containsKey("scriptPubKey")) {
                Map<String, Object> scriptPubKey = (Map<String, Object>) retrievedVout.get("scriptPubKey");
                if (scriptPubKey != null) {
                    if (scriptPubKey.containsKey("addresses")) {
                        this.addresses = (List<String>) scriptPubKey.get("addresses");
                    }
                }
            }
            if (retrievedVout.containsKey("value")) {
                this.value = new BigDecimal((retrievedVout.get("value")).toString()).setScale(8, RoundingMode.HALF_UP);
            } // TODO set scale is not working
        }
    }
}
