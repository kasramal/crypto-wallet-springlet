package com.demohouse.walletcore.services.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class WalletTransactionInquiryDTO implements Serializable {

    private String from;
    private String to;
    private BigDecimal value;
    private BigDecimal fee;
    private String transactionId;
    private boolean confirmed;
}
