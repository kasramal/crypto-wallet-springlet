package com.demohouse.walletcore.core.providers.bitcoincom.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class BitcoinComTransactionInputResponse implements Serializable {

    @JsonProperty("addr")
    private String address;
    private BigDecimal value;
}
