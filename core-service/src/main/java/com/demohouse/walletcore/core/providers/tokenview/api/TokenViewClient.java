package com.demohouse.walletcore.core.providers.tokenview.api;

import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewAddressInfo;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewToken;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewTransaction;
import com.demohouse.walletcore.core.providers.tokenview.api.models.TokenViewUTXO;
import com.demohouse.walletcore.core.providers.tokenview.api.models.request.TokenViewPushTransactionRequest;
import com.demohouse.walletcore.core.providers.tokenview.api.models.response.*;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.demohouse.walletcore.core.providers.tokenview.api.TokenViewConstants.*;

@Component
public class TokenViewClient {

    private final WebClient webClient;

    public TokenViewClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public TokenViewAddressInfo getAddressWithoutContractId(Coin coin, String address) {
        String uri = TokenViewConstants.getAddressInfoWithoutContractIdURL(coin, address);

        Mono<TokenViewAddressInfoResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewAddressInfoResponse.class);

        TokenViewAddressInfoResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            throw new RuntimeException();

        return response.getData().get(0);
    }

    public TokenViewAddressInfo getAddressWithContractId(Coin coin, String address) {
        String uri = TokenViewConstants.getAddressInfoWithContractIdURL(coin, address);

        Mono<TokenViewSingleAddressInfoResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewSingleAddressInfoResponse.class);

        TokenViewSingleAddressInfoResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            throw new RuntimeException();

        return response.getData();
    }

    public List<TokenViewTransaction> getPendingTransactionsOfAddress(Coin coin, String address) {
        String uri = TokenViewConstants.getPendingTransactionsURL(coin, address);

        Mono<TokenViewPendingTransactionResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewPendingTransactionResponse.class);

        TokenViewPendingTransactionResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            return new ArrayList<>();

        return response.getData();
    }

    public TokenViewAddressInfo getTransactionsByAddress(Coin coin, String address, int page, int size) {
        String uri = TokenViewConstants.getAddressInfoWithoutContractIdURL(coin, address, page, size);

        Mono<TokenViewAddressInfoResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewAddressInfoResponse.class);

        TokenViewAddressInfoResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);

        return response.getData().get(0);
    }


    public List<TokenViewUTXO> getAddressUTXOs(Coin coin, String address) {
        String uri = TokenViewConstants.getUTXOsUrl(coin, address);

        Mono<TokenViewUTXOResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewUTXOResponse.class);

        TokenViewUTXOResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);

        return response.getData();
    }

    public TokenViewTransaction getTransaction(Coin coin, String txHash) {
        String uri = TokenViewConstants.getTransactionDetailsURL(coin, txHash);

        Mono<TokenViewTransactionResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewTransactionResponse.class);

        TokenViewTransactionResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);

        return response.getData();
    }

    public List<TokenViewTransaction> getTokenTransactions(Coin coin, String address, String tokenAddress) {
        String uri = TokenViewConstants.getTransactionsOfTokenURL(coin, address, tokenAddress);

        Mono<TokenViewTokenTransactionsOfAddressResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewTokenTransactionsOfAddressResponse.class);

        TokenViewTokenTransactionsOfAddressResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);

        return response.getData();
    }


    public List<TokenViewToken> getTokenViewBalance(Coin coin, String address) {
        String uri = TokenViewConstants.getTokenBalanceURL(coin, address);

        Mono<TokenViewTokenResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(TokenViewTokenResponse.class);

        TokenViewTokenResponse response = bodyMono.block();

        if (!response.getCode().equals(SUCCESS_CODE))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);

        return response.getData();
    }


    public TokenViewPushTransactionResponse pushTransaction(Coin coin, String txHash) {
        String uri = TokenViewConstants.getPushURL(coin);

        TokenViewPushTransactionRequest pushTransactionRequest = new TokenViewPushTransactionRequest();
        pushTransactionRequest.setJsonrpc(DEFAULT_JSON_RPC);
        pushTransactionRequest.setId(DEFAULT_PUSH_ID);
        String method = isEthereum(coin) ? ETH_PUSH_METHOD : BTC_PUSH_METHOD;
        pushTransactionRequest.setMethod(method);
        pushTransactionRequest.setParams(Collections.singletonList(txHash));

        Mono<TokenViewPushTransactionResponse> bodyMono = webClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(pushTransactionRequest))
                .retrieve().bodyToMono(TokenViewPushTransactionResponse.class);

        TokenViewPushTransactionResponse response = bodyMono.block();

        if (response.getResult() == null) {
            if (isEthereum(coin))
                throw new RuntimeException();
            throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
        }

        return response;
    }

    private boolean isEthereum(Coin coin) {
        return coin.equals(Coin.ETHEREUM) || coin.equals(Coin.ETHEREUM_CLASSIC);
    }

}
