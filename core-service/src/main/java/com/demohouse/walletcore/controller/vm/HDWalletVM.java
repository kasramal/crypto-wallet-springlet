package com.demohouse.walletcore.controller.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Getter
@Setter
public class HDWalletVM implements Serializable {

    private String mnemonic;
    private String address;
    private String publicKey;
    private String xPublicKey;
}
