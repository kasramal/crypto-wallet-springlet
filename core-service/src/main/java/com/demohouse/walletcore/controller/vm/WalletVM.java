package com.demohouse.walletcore.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class WalletVM implements Serializable {

    private String address;
}
