package com.demohouse.walletcore.core.providers.tronprovider.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TronGridTransactionData implements Serializable {

    private String ref_block_bytes;
    private String ref_block_hash;
    private Long expiration;
    private BigInteger timestamp;

}
