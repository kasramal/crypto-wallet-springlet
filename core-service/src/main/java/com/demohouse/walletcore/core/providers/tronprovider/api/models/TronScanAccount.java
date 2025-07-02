package com.demohouse.walletcore.core.providers.tronprovider.api.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TronScanAccount {
    private String address;
    private long balance;
    private long totalTransactionCount;
}
