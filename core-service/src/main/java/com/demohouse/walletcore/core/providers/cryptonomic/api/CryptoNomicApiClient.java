package com.demohouse.walletcore.core.providers.cryptonomic.api;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.providers.cryptonomic.api.model.request.CryptoNomicForgeOperationRequest;
import com.demohouse.walletcore.core.providers.cryptonomic.api.model.request.CryptoNomicPreApplyOperationRequest;
import com.demohouse.walletcore.core.providers.cryptonomic.api.model.response.CryptoNomicPreApplyOperationResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Component
public class CryptoNomicApiClient {

    private final WebClient webClient;

    public CryptoNomicApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getHash() {
        String hash = webClient
                .get()
                .uri(CryptoNomicApiConstants.getGenerateHashURL())
                .header(CryptoNomicApiConstants.API_KEY_NAME, CryptoNomicApiConstants.API_KEY_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (hash != null) {
            return hash.replace("\"", "").replace("\n", "");
        } else {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
        }
    }

    public Long getCounter(String address) {
        String counter = webClient
                .get()
                .uri(CryptoNomicApiConstants.getCounterURL(address))
                .header(CryptoNomicApiConstants.API_KEY_NAME, CryptoNomicApiConstants.API_KEY_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (counter != null) {
            String replacementCounter = counter.replace("\"", "").replace("\n", "");
            return Long.parseLong(replacementCounter);
        } else {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
        }
    }

    public Long getBalance(String address) {
        String balance = webClient
                .get()
                .uri(CryptoNomicApiConstants.getBalanceURL(address))
                .header(CryptoNomicApiConstants.API_KEY_NAME, CryptoNomicApiConstants.API_KEY_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (balance != null) {
            String replacementBalance = balance.replace("\"", "").replace("\n", "");
            return Long.parseLong(replacementBalance);
        } else {
            return 0L;
        }
    }

    public String getManagerKey(String address) {
        String managerKey = webClient
                .get()
                .uri(CryptoNomicApiConstants.getManagerKeyURL(address))
                .header(CryptoNomicApiConstants.API_KEY_NAME, CryptoNomicApiConstants.API_KEY_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (managerKey != null) {
            String replacement = managerKey.replace("\"", "").replace("\n", "");
            return replacement.equals("null") ? null : replacement;
        } else {
            return null;
        }
    }

    public String forgeOperation(CryptoNomicForgeOperationRequest request) {
        String result = webClient
                .post()
                .uri(CryptoNomicApiConstants.getForgeOperationURL())
                .header(CryptoNomicApiConstants.API_KEY_NAME, CryptoNomicApiConstants.API_KEY_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (result != null) {
            return result.replace("\"", "").replace("\n", "");
        } else {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
        }
    }

    public Set<CryptoNomicPreApplyOperationResponse> preApplyOperation(CryptoNomicPreApplyOperationRequest request) {
        Set<CryptoNomicPreApplyOperationResponse> results = webClient
                .post()
                .uri(CryptoNomicApiConstants.getPreApplyOperationURL())
                .header(CryptoNomicApiConstants.API_KEY_NAME, CryptoNomicApiConstants.API_KEY_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(Set.class)
                .block();
        if (results != null) {
            return results;
        } else {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
        }
    }

    public String injectOperation(String operationBytes) {
        String result = webClient
                .post()
                .uri(CryptoNomicApiConstants.getInjectOperationURL())
                .header(CryptoNomicApiConstants.API_KEY_NAME, CryptoNomicApiConstants.API_KEY_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(operationBytes))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (result != null) {
            return result.replace("\"", "").replace("\n", "");
        } else {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
        }
    }
}




