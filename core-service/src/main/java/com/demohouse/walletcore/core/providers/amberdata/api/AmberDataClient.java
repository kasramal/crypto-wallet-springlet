package com.demohouse.walletcore.core.providers.amberdata.api;

import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataAccountTransactions;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataAddressInfo;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataToken;
import com.demohouse.walletcore.core.providers.amberdata.api.models.AmberDataTransaction;
import com.demohouse.walletcore.core.providers.amberdata.api.models.request.AmberDataPushTransactionRequest;
import com.demohouse.walletcore.core.providers.amberdata.api.models.response.*;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static com.demohouse.walletcore.core.providers.amberdata.api.AmberDataConstants.DEFAULT_JSON_RPC;
import static com.demohouse.walletcore.core.providers.amberdata.api.AmberDataConstants.DEFAULT_PUSH_ID;


@Component
public class AmberDataClient {

    private final WebClient webClient;

    @Value("${wallet.crypto.api.amberdata.apiKey}")
    private String AMBER_DATA_API_KEY;

    public AmberDataClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private String getBlockChainId(Coin coin) {
        switch (coin) {
            case ETHEREUM:
            case TETHER:
                return "ethereum-mainnet";
            case BITCOIN:
                return "bitcoin-mainnet";
            case BITCOINSV:
                return "bitcoin-sv-mainnet";
            case LITECOIN:
                return "litecoin-mainnet";
            case ZCASH:
                return "zcash-mainnet";
            default:
                throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
        }
    }

    private String getJsonRPCBlockChainId(Coin coin) {
        switch (coin) {
            case ETHEREUM:
            case TETHER:
                return "1c9c969065fcd1cf";
            case BITCOIN:
                return "408fa195a34b533de9ad9889f076045e";
            default:
                throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
        }
    }

    public AmberDataAddressInfo getAddress(Coin coin, String address) {

        String blockChainId = getBlockChainId(coin);
        String uri = AmberDataConstants.getAddressInfoURL(address);

        Mono<AmberDataAddressResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(AmberDataConstants.HEADER_TOKEN_KEY, AMBER_DATA_API_KEY)
                .header(AmberDataConstants.HEADER_BLOCKCHAIN_ID_KEY, blockChainId)
                .retrieve().bodyToMono(AmberDataAddressResponse.class);

        return bodyMono.block().getPayload();
    }


    public AmberDataTransaction getTransaction(Coin coin, String transactionHash) {
        String blockChainId = getBlockChainId(coin);
        String uri = AmberDataConstants.getTransactionsURL(transactionHash);

        Mono<AmberDataTransactionResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(AmberDataConstants.HEADER_TOKEN_KEY, AMBER_DATA_API_KEY)
                .header(AmberDataConstants.HEADER_BLOCKCHAIN_ID_KEY, blockChainId)
                .retrieve().bodyToMono(AmberDataTransactionResponse.class);

        return bodyMono.block().getPayload();
    }

    public String pushTransaction(Coin coin, String transactionHex) {
        String uri = AmberDataConstants.getPushTransactionURL(AMBER_DATA_API_KEY);

        AmberDataPushTransactionRequest pushTransactionRequest = new AmberDataPushTransactionRequest();
        pushTransactionRequest.setJsonrpc(DEFAULT_JSON_RPC);
        pushTransactionRequest.setId(DEFAULT_PUSH_ID);
        pushTransactionRequest.setMethod(AmberDataConstants.getJsonRPCPushMethod(coin));
        pushTransactionRequest.setParams(Collections.singletonList(transactionHex));

        Mono<AmberDataPushTransactionResponse> bodyMono = webClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AmberDataConstants.HEADER_BLOCKCHAIN_ID_KEY, this.getJsonRPCBlockChainId(coin))
                .body(BodyInserters.fromValue(pushTransactionRequest))
                .retrieve().bodyToMono(AmberDataPushTransactionResponse.class);

        AmberDataPushTransactionResponse response = bodyMono.block();

        return response.getResult();
    }

    public AmberDataAccountTransactions getTransactionOfAddress(Coin coin, String address) {
        String blockChainId = getBlockChainId(coin);
        String uri = AmberDataConstants.getTransactionsOfAddressURL(address);

        Mono<AmberDataAccountTransactionsResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(AmberDataConstants.HEADER_TOKEN_KEY, AMBER_DATA_API_KEY)
                .header(AmberDataConstants.HEADER_BLOCKCHAIN_ID_KEY, blockChainId)
                .retrieve().bodyToMono(AmberDataAccountTransactionsResponse.class);

        return bodyMono.block().getPayload();
    }

    public AmberDataAccountTransactions getPendingTransactionsOfAddress(Coin coin, String address) {
        String blockChainId = getBlockChainId(coin);
        String uri = AmberDataConstants.getPendingTransactionsOfAddressURL(address);

        Mono<AmberDataAccountTransactionsResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(AmberDataConstants.HEADER_TOKEN_KEY, AMBER_DATA_API_KEY)
                .header(AmberDataConstants.HEADER_BLOCKCHAIN_ID_KEY, blockChainId)
                .retrieve().bodyToMono(AmberDataAccountTransactionsResponse.class);

        return bodyMono.block().getPayload();
    }

    public AmberDataAccountTransactions getPendingTransactionOfEthereumAddress(Coin coin, String address) {
        String blockChainId = getBlockChainId(coin);
        String uri = AmberDataConstants.getPendingTransactionsOfAddressURL(address);

        Mono<AmberDataAccountTransactionsResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(AmberDataConstants.HEADER_TOKEN_KEY, AMBER_DATA_API_KEY)
                .header(AmberDataConstants.HEADER_BLOCKCHAIN_ID_KEY, blockChainId)
                .retrieve().bodyToMono(AmberDataAccountTransactionsResponse.class);

        return bodyMono.block().getPayload();
    }

    public List<AmberDataToken> getTokensOfAddress(Coin coin, String address) {
        String blockChainId = getBlockChainId(coin);
        String uri = AmberDataConstants.getTokensURL(address);

        Mono<AmberDataTokensResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(AmberDataConstants.HEADER_TOKEN_KEY, AMBER_DATA_API_KEY)
                .header(AmberDataConstants.HEADER_BLOCKCHAIN_ID_KEY, blockChainId)
                .retrieve().bodyToMono(AmberDataTokensResponse.class);

        return bodyMono.block().getPayload().getRecords();
    }


}
