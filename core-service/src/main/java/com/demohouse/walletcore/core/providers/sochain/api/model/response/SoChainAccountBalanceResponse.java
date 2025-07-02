package com.demohouse.walletcore.core.providers.sochain.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SoChainAccountBalanceResponse implements Serializable {

    private String network;
    private String address;
    @JsonProperty("confirmed_balance")
    private String balance;
    @JsonProperty("unconfirmed_balance")
    private String unConfirmedBalance;
}
