package com.demohouse.walletcore.core.providers.tronprovider.api;

import com.demohouse.walletcore.core.providers.tronprovider.api.models.TronScanAccount;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.TronScanTransaction;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.enumorate.TronScanConfirmationStatus;
import com.demohouse.walletcore.core.providers.tronprovider.api.models.response.TronScanTransactionListResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class TronScanApisClient {

    private final WebClient webClient;

    public TronScanApisClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public TronScanAccount getAccount(String address) {
        String path = String.format("%s?address=%s", TronScanApisConstants.getAccountURL(), address);
        return webClient
                .get()
                .uri(path)
                .retrieve()
                .bodyToMono(TronScanAccount.class)
                .block();
    }

    public List<TronScanTransaction> getTransactions(String address, TronScanConfirmationStatus status) {
        String path = String.format("%s?address=%s&tokens=_&confirm=%s", TronScanApisConstants.getTransactionsURL(), address, status.getCode());
        Mono<TronScanTransactionListResponse> bodyMono = webClient
                .get()
                .uri(path)
                .retrieve()
                .bodyToMono(TronScanTransactionListResponse.class);
        TronScanTransactionListResponse response = bodyMono.block();
        return response != null ? response.getTotal() > 0 ? response.getData() : new ArrayList<>() : new ArrayList<>();
    }

    public List<TronScanTransaction> getTransactions(String address) {
        String path = String.format("%s?address=%s&tokens=_", TronScanApisConstants.getTransactionsURL(), address);
        Mono<TronScanTransactionListResponse> bodyMono = webClient
                .get()
                .uri(path)
                .retrieve()
                .bodyToMono(TronScanTransactionListResponse.class);
        TronScanTransactionListResponse response = bodyMono.block();
        return response != null ? response.getTotal() > 0 ? response.getData() : new ArrayList<>() : new ArrayList<>();
    }

    public TronScanTransaction getTransactionInfo(String txHash) {
        String path = String.format("%s?hash=%s", TronScanApisConstants.getTransactionInfoURL(), txHash);
        return webClient
                .get()
                .uri(path)
                .retrieve()
                .bodyToMono(TronScanTransaction.class)
                .block();
    }
}
