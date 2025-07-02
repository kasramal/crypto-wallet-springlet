package com.demohouse.walletcore.core.providers.tronprovider.api;

import com.demohouse.walletcore.core.thirdparty.crypto.wallet;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.request.TronGridCreateTransactionRequest;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.request.TronGridPushTransactionRequest;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.response.TronGridCreateTransactionResponse;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.response.TronGridPushTransactionResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class TronGridApisClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public TronGridApisClient(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public TronGridCreateTransactionResponse createTransaction(String from, String to, long amount) {
        TronGridCreateTransactionRequest request = TronGridCreateTransactionRequest.builder()
                .ownerAddress("41" + wallet.address_decode(from, "tron", false).l.toString(16))
                .toAddress("41" + wallet.address_decode(to, "tron", false).l.toString(16))
                .amount(amount)
                .build();
        Mono<String> bodyMono = webClient
                .post()
                .uri(TronGridApisConstants.getCreateTransactionURL())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve()
                .bodyToMono(String.class);
        try {
            return objectMapper.readValue(bodyMono.block(), TronGridCreateTransactionResponse.class);
        } catch (IOException e) {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INSUFFICIENT_BALANCE);
        }
    }

    public TronGridPushTransactionResponse pushTransaction(String txHex) {
        TronGridPushTransactionRequest request = TronGridPushTransactionRequest.builder()
                .transaction(txHex)
                .build();
        Mono<String> bodyMono = webClient
                .post()
                .uri(TronGridApisConstants.getPushTransactionURL())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve()
                .bodyToMono(String.class);
        try {
            return objectMapper.readValue(bodyMono.block(), TronGridPushTransactionResponse.class);
        } catch (IOException e) {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
        }
    }
}
