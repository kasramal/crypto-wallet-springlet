package com.demohouse.walletcore.core.providers.tronprovider.api.models.response;

import com.demohouse.walletcore.core.providers.tronprovider.api.models.TronScanTransaction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TronScanTransactionListResponse {

    private int total;
    private int rangeTotal;
    private List<TronScanTransaction> data = new ArrayList<>();
}
