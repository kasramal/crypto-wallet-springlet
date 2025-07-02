package com.demohouse.walletcore.core.providers.btcexplorer.api;

import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerAddress;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransaction;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransactionList;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.response.BtcExplorerAddressResponse;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.response.BtcExplorerTransactionResponse;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.response.BtcExplorerTransactionsOfAddressResponse;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BtcExplorerClient {

    private final WebClient webClient;

    public BtcExplorerClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public BtcExplorerAddress getAddress(Coin coin, String address) {

        String uri = BtcExplorerConstants.getAddressInfoURL(coin, address);

        Mono<BtcExplorerAddressResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(BtcExplorerAddressResponse.class);

        return bodyMono.block().getData();
    }


    public BtcExplorerTransaction getTransaction(Coin coin, String transactionHash) {
        String uri = BtcExplorerConstants.getTransactionURL(coin, transactionHash);

        Mono<BtcExplorerTransactionResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(BtcExplorerTransactionResponse.class);

        return bodyMono.block().getData();
    }

    public BtcExplorerTransactionList getTransactionsOfAddress(Coin coin, String address) {
        String uri = BtcExplorerConstants.getTransactionsOfAddressURL(coin, address);

        Mono<BtcExplorerTransactionsOfAddressResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(BtcExplorerTransactionsOfAddressResponse.class);

        return bodyMono.block().getData();
    }

}
