package com.demohouse.walletcore.core.fees.api;

import com.demohouse.walletcore.core.fees.api.model.EarnBitcoinFee;
import com.demohouse.walletcore.core.fees.api.model.EthGasStationPrice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Component
public class WalletFeeModule {

    private final static String ETH_GAS_STATION_URL = "https://ethgasstation.info/api/ethgasAPI.json";
    private final static String EARN_RECOMMENDED_URL = "https://bitcoinfees.earn.com/api/v1/fees/recommended";
    private final WebClient webClient;

    private EthGasStationPrice gasPrice;
    private EarnBitcoinFee bitcoinFee;

    public WalletFeeModule() {
        this.webClient = WebClient.create();
    }


    private void updateFeeUtils() {
        updateBitcoinFee();
        updateEthereumGasPrice();
    }

    @PostConstruct
    private void init() {
        bitcoinFee = new EarnBitcoinFee(200L, 100L, 50L);
        gasPrice = new EthGasStationPrice();
        gasPrice.setFastest(BigDecimal.valueOf(600L));
        gasPrice.setFast(BigDecimal.valueOf(440L));
        gasPrice.setAverage(BigDecimal.valueOf(390L));
        gasPrice.setSafeLow(BigDecimal.valueOf(330L));
//        updateFeeUtils();
    }

    private void updateBitcoinFee() {
        try {
            Mono<EarnBitcoinFee> bodyMono = webClient.get()
                    .uri(EARN_RECOMMENDED_URL)
                    .retrieve().bodyToMono(EarnBitcoinFee.class);

            this.bitcoinFee = bodyMono.block();

        } catch (Exception ignored) { }
    }

    private void updateEthereumGasPrice() {
        try {
            Mono<EthGasStationPrice> bodyMono = webClient.get()
                    .uri(ETH_GAS_STATION_URL)
                    .retrieve().bodyToMono(EthGasStationPrice.class);

            this.gasPrice = bodyMono.block();

        } catch (Exception ignored) { }
    }

    public EthGasStationPrice getGasPrice() {
        return gasPrice;
    }

    public EarnBitcoinFee getBitcoinFee() {
        return bitcoinFee;
    }

    @Scheduled(cron = "43 1/5 * * * ?")
    private void refreshFeeUtils() {
        updateFeeUtils();
    }
}
