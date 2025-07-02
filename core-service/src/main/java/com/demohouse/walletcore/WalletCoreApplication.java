package com.demohouse.walletcore;

import com.demohouse.walletcore.configuration.properties.AuthenticationProperties;
import com.demohouse.walletcore.configuration.properties.CoinTransactionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EnableConfigurationProperties({AuthenticationProperties.class, CoinTransactionProperties.class})
public class WalletCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletCoreApplication.class, args);
    }

}
