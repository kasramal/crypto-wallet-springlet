package com.demohouse.walletcore.core.providers.sochain.api;

import com.demohouse.walletcore.core.providers.sochain.api.model.response.*;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.providers.sochain.api.model.request.SoChainPushTransactionRequest;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SoChainClient {

    private final WebClient webClient;

    public SoChainClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public SoChainPushTransactionResponse pushTransaction(Coin coin, String transactionHex) {
        SoChainPushTransactionRequest request = new SoChainPushTransactionRequest();
        request.setTransactionHex(transactionHex);
        SoChainPushTransactionPayloadResponse response = webClient.post()
                .uri(SoChainConstants.pushTransactionURL(coin))
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(SoChainPushTransactionPayloadResponse.class)
                .block();
        if (response != null) {
            if (SoChainConstants.SUCCESS_RESPONSE_STATUS.equals(response.getStatus())) {
                return response.getData();
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public SoChainTransactionByHashResponse getTransactionByHash(Coin coin, String transactionId) {
        SoChainTransactionByHashPayloadResponse response = webClient.get()
                .uri(SoChainConstants.getTransactionByHashURL(coin, transactionId))
                .retrieve()
                .bodyToMono(SoChainTransactionByHashPayloadResponse.class)
                .block();
        if (response != null) {
            if (SoChainConstants.SUCCESS_RESPONSE_STATUS.equals(response.getStatus())) {
                return response.getData();
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public SoChainTransactionByAddressResponse getReceivedTransactionByAddress(Coin coin, String address) {
        SoChainTransactionByAddressPayloadResponse response = webClient.get()
                .uri(SoChainConstants.getReceivedTransactionByAddressURL(coin, address))
                .retrieve()
                .bodyToMono(SoChainTransactionByAddressPayloadResponse.class)
                .block();
        if (response != null) {
            if (SoChainConstants.SUCCESS_RESPONSE_STATUS.equals(response.getStatus())) {
                return response.getData();
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public SoChainTransactionByAddressResponse getUnSpentTransactionByAddress(Coin coin, String address) {
        SoChainTransactionByAddressPayloadResponse response = webClient.get()
                .uri(SoChainConstants.getUnSpentTransactionByAddressURL(coin, address))
                .retrieve()
                .bodyToMono(SoChainTransactionByAddressPayloadResponse.class)
                .block();
        if (response != null) {
            if (SoChainConstants.SUCCESS_RESPONSE_STATUS.equals(response.getStatus())) {
                return response.getData();
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public SoChainAccountBalanceResponse getAccountBalance(Coin coin, String address) {
        SoChainAccountBalancePayloadResponse response = webClient.get()
                .uri(SoChainConstants.getAddressBalanceURL(coin, address))
                .retrieve()
                .bodyToMono(SoChainAccountBalancePayloadResponse.class)
                .block();
        if (response != null) {
            if (SoChainConstants.SUCCESS_RESPONSE_STATUS.equals(response.getStatus())) {
                return response.getData();
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

}
