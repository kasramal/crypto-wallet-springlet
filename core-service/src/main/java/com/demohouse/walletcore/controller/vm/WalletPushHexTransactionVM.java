package com.demohouse.walletcore.controller.vm;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class WalletPushHexTransactionVM implements Serializable {

    @NotNull
    private String transactionHex;
}
