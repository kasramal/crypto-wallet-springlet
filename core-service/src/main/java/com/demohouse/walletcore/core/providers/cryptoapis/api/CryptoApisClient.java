package com.demohouse.walletcore.core.providers.cryptoapis.api;

import com.demohouse.walletcore.core.providers.cryptoapis.api.models.*;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.responses.*;
import com.demohouse.walletcore.core.providers.cryptoapis.api.models.requests.CryptoApisPushTransactionRequest;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CryptoApisClient {

    private final WebClient webClient;

    @Value("${wallet.crypto.api.cryptoapis.apiKey}")
    private String CRYPTO_APIS_API_KEY;

    public CryptoApisClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private String getCoinName(Coin coin) {
        switch (coin) {
            case BITCOIN:
                return "btc";
            case BITCOINSV:
                return "bsv";
            case BITCOINCASH:
                return "bch";
            case LITECOIN:
                return "ltc";
            case DOGECOIN:
                return "doge";
            case TETHER:
            case ETHEREUM:
                return "eth";
            case ETHEREUM_CLASSIC:
                return "etc";
        }
        return "";
    }

    public List<CryptoApisTransaction> getConfirmedTransactions(Coin coin, String address) {

        String uri = CryptoApisConstants.getAddressConfirmedTransactionsURL(getCoinName(coin), address);

        Mono<CryptoApisTransactionListResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisTransactionListResponse.class);

        CryptoApisTransactionListResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }


    public CryptoApisTransaction getTransactions(Coin coin, String txHash) {
        String url = CryptoApisConstants.getTransactionURL(getCoinName(coin), txHash);
        Mono<CryptoApisTransactionResponse> bodyMono = webClient.get()
                .uri(url)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisTransactionResponse.class);

        CryptoApisTransactionResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }

    public CryptoApisTransaction getTransactionByHash(Coin coin, String txHash) {
        String url = CryptoApisConstants.getFullTransactionURL(getCoinName(coin), txHash);
        Mono<CryptoApisTransactionResponse> bodyMono = webClient.get()
                .uri(url)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisTransactionResponse.class);

        CryptoApisTransactionResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }

    public List<CryptoApisTransaction> getUnconfirmedTransactions(Coin coin, String address) {
        String uri = CryptoApisConstants.getAddressUnconfirmedTransactionsURL(getCoinName(coin), address);
        Mono<CryptoApisTransactionListResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisTransactionListResponse.class);

        CryptoApisTransactionListResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }


    public CryptoApisAddress getAddress(Coin coin, String address) {
        String uri = CryptoApisConstants.getAddressURL(getCoinName(coin), address);
        Mono<CryptoApisAddressResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisAddressResponse.class);

        CryptoApisAddressResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }

    public CryptoApisAddressNonce getAddressNonce(Coin coin, String address) {
        String uri = CryptoApisConstants.getAddressURL(getCoinName(coin), address) + "/nonce";
        Mono<CryptoApisNonceResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisNonceResponse.class);

        CryptoApisNonceResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }

    public List<CryptoApisAddressTokenTransfer> getAddressTokenTransfers(Coin coin, String address) {
        String uri = CryptoApisConstants.getAddressTokenTransferURL(getCoinName(coin), address);
        Mono<CryptoApisAddressTokenTransfersResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisAddressTokenTransfersResponse.class);

        CryptoApisAddressTokenTransfersResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }

    public List<CryptoApisAddressToken> getAddressTokens(Coin coin, String address) {
        String uri = CryptoApisConstants.getAddressTokensURL(getCoinName(coin), address);
        Mono<CryptoApisAddressTokensResponse> bodyMono = webClient.get()
                .uri(uri)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .retrieve().bodyToMono(CryptoApisAddressTokensResponse.class);

        CryptoApisAddressTokensResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }

    public CryptoApisTransaction pushTransaction(Coin coin, String txData) {
        CryptoApisPushTransactionRequest request = new CryptoApisPushTransactionRequest();
        request.setHex(txData);
        String uri = CryptoApisConstants.getPushTransactionUrl(getCoinName(coin));
        Mono<CryptoApisTransactionResponse> bodyMono = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header(CryptoApisConstants.HEADER_TOKEN_KEY, CRYPTO_APIS_API_KEY)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(CryptoApisTransactionResponse.class);

        CryptoApisTransactionResponse response = bodyMono.block();
        return response != null ? response.getPayload() : null;
    }
}
