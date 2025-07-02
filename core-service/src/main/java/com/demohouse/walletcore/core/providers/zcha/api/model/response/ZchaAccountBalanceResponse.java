package com.demohouse.walletcore.core.providers.zcha.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ZchaAccountBalanceResponse implements Serializable {

    private String address;
    private BigDecimal balance;
}
