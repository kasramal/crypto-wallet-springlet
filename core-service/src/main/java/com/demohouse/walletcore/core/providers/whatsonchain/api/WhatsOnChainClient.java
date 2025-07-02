package com.demohouse.walletcore.core.providers.whatsonchain.api;

import com.demohouse.walletcore.core.providers.whatsonchain.api.model.request.WhatsOnChainPushTransactionRequest;
import com.demohouse.walletcore.core.providers.whatsonchain.api.model.response.WhatsOnChainAccountBalanceResponse;
import com.demohouse.walletcore.core.providers.whatsonchain.api.model.response.WhatsOnChainTransactionHistoryResponse;
import com.demohouse.walletcore.core.providers.whatsonchain.api.model.response.WhatsOnChainTransactionResponse;
import com.demohouse.walletcore.core.providers.whatsonchain.api.model.response.WhatsOnChainUnSpentTransactionResponse;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class WhatsOnChainClient {

    private final WebClient webClient;

    public WhatsOnChainClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public WhatsOnChainAccountBalanceResponse getAccountBalance(String address) {
        WhatsOnChainAccountBalanceResponse response = webClient
                .get()
                .uri(WhatsOnChainConstants.getAccountBalanceURL(address))
                .retrieve()
                .bodyToMono(WhatsOnChainAccountBalanceResponse.class)
                .block();
        if (response != null)
            return response;
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public WhatsOnChainTransactionResponse getTransactionByHash(String transactionId) {
        WhatsOnChainTransactionResponse response = webClient
                .get()
                .uri(WhatsOnChainConstants.getTransactionByHashURL(transactionId))
                .retrieve()
                .bodyToMono(WhatsOnChainTransactionResponse.class)
                .block();
        if (response != null)
            return response;
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public List<WhatsOnChainUnSpentTransactionResponse> getUnSpentTransactions(String address) {
        List<WhatsOnChainUnSpentTransactionResponse> response = webClient
                .get()
                .uri(WhatsOnChainConstants.getUnSpentTransactionByAddressURL(address))
                .retrieve()
                .bodyToFlux(WhatsOnChainUnSpentTransactionResponse.class)
                .collectList()
                .block();
        if (response != null)
            return response;
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public List<WhatsOnChainTransactionHistoryResponse> getTransactionHistory(String address) {
        List<WhatsOnChainTransactionHistoryResponse> response = webClient
                .get()
                .uri(WhatsOnChainConstants.getTransactionByAddressURL(address))
                .retrieve()
                .bodyToFlux(WhatsOnChainTransactionHistoryResponse.class)
                .collectList()
                .block();
        if (response != null)
            return response;
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public String pushTransaction(String transactionHex) {
        WhatsOnChainPushTransactionRequest request = new WhatsOnChainPushTransactionRequest();
        request.setTransactionHex(transactionHex);
        String response = webClient
                .post()
                .uri(WhatsOnChainConstants.pushTransactionURL())
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (response != null)
            return response.replace("\"", "").replace("\n", "");
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }
}
