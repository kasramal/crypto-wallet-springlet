package com.demohouse.walletcore.core.providers.whatsonchain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class WhatsOnChainAccountBalanceResponse implements Serializable {

    @JsonProperty("confirmed")
    private BigDecimal balance;
    private BigDecimal unconfirmed;
}
