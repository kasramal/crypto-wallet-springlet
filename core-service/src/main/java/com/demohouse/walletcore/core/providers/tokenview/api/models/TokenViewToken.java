package com.demohouse.walletcore.core.providers.tokenview.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenViewToken implements Serializable {

    private String hash;
    private BigDecimal balance;
    private BigDecimal amount;
    private String name;
    private String symbol;

    @JsonProperty("tokenInfo")
    private void unPackTokenInfo(Map<String, Object> tokenInfo) {
        if (tokenInfo != null) {
            if (tokenInfo.containsKey("s")) {
                this.symbol = (String) tokenInfo.get("s");
            }
            if (tokenInfo.containsKey("f")) {
                this.name = (String) tokenInfo.get("f");
            }
            if (tokenInfo.containsKey("t")) {
                this.amount = new BigDecimal((String) tokenInfo.get("t"));
            }
        }
    }
}
