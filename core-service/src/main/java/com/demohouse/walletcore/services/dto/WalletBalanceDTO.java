package com.demohouse.walletcore.services.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletBalanceDTO {

    private BigDecimal micro;
    private BigDecimal macro;
}
