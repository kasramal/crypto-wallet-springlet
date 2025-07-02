package com.demohouse.walletcore.core.providers.zcha.api;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.providers.zcha.api.model.response.ZchaAccountBalanceResponse;
import com.demohouse.walletcore.core.providers.zcha.api.model.response.ZchaNetworkResponse;
import com.demohouse.walletcore.core.providers.zcha.api.model.response.ZchaTransactionResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ZchaClient {

    private final WebClient webClient;

    public ZchaClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ZchaNetworkResponse getNetwork() {
        ZchaNetworkResponse response = webClient
                .get()
                .uri(ZchaConstants.getNetworkURL())
                .retrieve()
                .bodyToMono(ZchaNetworkResponse.class)
                .block();
        if (response != null) {
            return response;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public ZchaAccountBalanceResponse getAccountBalance(String address) {
        ZchaAccountBalanceResponse response = webClient
                .get()
                .uri(ZchaConstants.getAccountBalanceURL(address))
                .retrieve()
                .bodyToMono(ZchaAccountBalanceResponse.class)
                .block();
        if (response != null) {
            return response;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public ZchaTransactionResponse getTransactionByHash(String txHash) {
        ZchaTransactionResponse response = webClient
                .get()
                .uri(ZchaConstants.getTransactionByHash(txHash))
                .retrieve()
                .bodyToMono(ZchaTransactionResponse.class)
                .block();
        if (response != null) {
            return response;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public List<ZchaTransactionResponse> getTransactionByAddress(String address) {
        List<ZchaTransactionResponse> response = webClient
                .get()
                .uri(ZchaConstants.getTransactionByAddress(address))
                .retrieve()
                .bodyToFlux(ZchaTransactionResponse.class)
                .collectList()
                .block();
        if (response != null) {
            return response;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

}
