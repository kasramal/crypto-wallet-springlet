package com.demohouse.walletcore.core.providers.zcha.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZchaNetworkResponse implements Serializable {
    private Long blockNumber;
}
