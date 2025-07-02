package com.demohouse.walletcore.core.hdwallets;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HDWalletCredentials {
    private String mnemonic;
    private String password;
}
