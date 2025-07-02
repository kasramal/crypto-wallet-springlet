package com.demohouse.walletcore.core.providers.tronprovider.api.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class TronScanTransaction {

    private BigDecimal block;
    private String hash;
    private Long timestamp;
    private String ownerAddress;
    private List<String> toAddressList;
    private String toAddress;
    private Integer contractType;
    private boolean confirmed;
    private boolean revert;
    private String amount;
    private TronScanTransactionContract contractData;
}
