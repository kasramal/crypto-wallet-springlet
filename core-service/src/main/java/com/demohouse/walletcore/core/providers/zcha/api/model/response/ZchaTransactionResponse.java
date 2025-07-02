package com.demohouse.walletcore.core.providers.zcha.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class ZchaTransactionResponse implements Serializable {

    private String hash;
    private BigDecimal fee;
    private Long blockHeight;
    private BigDecimal value;
    private BigDecimal outputValue;
    @JsonProperty("vin")
    private Set<ZchaInputResponse> inputs;
}
