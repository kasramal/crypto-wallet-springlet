package com.demohouse.walletcore.services.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class WalletTransactionDTO implements Serializable {

    @NotNull
    private String from;
    private String privateKey;
    @NotNull
    private String to;
    private BigDecimal value;
}
