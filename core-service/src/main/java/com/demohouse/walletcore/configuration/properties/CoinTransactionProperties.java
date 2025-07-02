package com.demohouse.walletcore.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "wallet.core.transaction")
public class CoinTransactionProperties {

    private Map<String, Value> coins;

    @Getter
    @Setter
    public static class Value {
        private long minValue;
    }
}
