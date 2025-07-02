package com.demohouse.walletcore.core.providers.tronprovider.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.TronGridTransactionData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TronGridCreateTransactionResponse implements Serializable {

    private String txID;
    private TronGridTransactionData raw_data;
    private String raw_data_hex;
}
