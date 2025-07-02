package com.demohouse.walletcore.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class WalletSignTransactionVM implements Serializable {

    private String signedTransaction;
}
