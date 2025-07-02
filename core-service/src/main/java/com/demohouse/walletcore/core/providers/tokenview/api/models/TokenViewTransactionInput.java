package com.demohouse.walletcore.core.providers.tokenview.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenViewTransactionInput implements Serializable {

    private String address;
    private Long inputNo;
    private BigDecimal value;
    private String txid;
    private int outputNo;

    @JsonProperty("received_from")
    private void unPackReceivedFrom(Map<String, Object> receivedFrom) {
        if (receivedFrom != null) {
            if (receivedFrom.containsKey("txid")) {
                this.txid = (String) receivedFrom.get("txid");
            }
            if (receivedFrom.containsKey("output_no")) {
                this.outputNo = (int) receivedFrom.get("output_no");
            }
        }
    }
}
