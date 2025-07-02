package com.demohouse.walletcore.core.providers.bitcoincom.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BitcoinComTransactionResponse implements Serializable {

    @JsonProperty("txid")
    private String transactionId;
    private Long confirmations;
    private BigDecimal valueOut;
    private BigDecimal valueIn;
    private BigDecimal fees;
    @JsonProperty("vin")
    private List<BitcoinComTransactionInputResponse> inputs;
    @JsonProperty("vout")
    private List<BitcoinComTransactionOutputResponse> outputs;
}
