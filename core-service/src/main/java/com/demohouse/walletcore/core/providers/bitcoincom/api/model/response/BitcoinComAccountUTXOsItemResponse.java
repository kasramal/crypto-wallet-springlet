package com.demohouse.walletcore.core.providers.bitcoincom.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class BitcoinComAccountUTXOsItemResponse implements Serializable {

    @JsonProperty("txid")
    private String transactionId;
    @JsonProperty("vout")
    private Long outputNumber;
    private BigDecimal amount;
    private BigInteger height;
    private Long confirmations;
}
