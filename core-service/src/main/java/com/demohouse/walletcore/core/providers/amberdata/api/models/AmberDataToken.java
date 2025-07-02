package com.demohouse.walletcore.core.providers.amberdata.api.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AmberDataToken {

    private String address;
    private String holder;
    private BigDecimal amount;
    private String name;
    private String symbol;
}
