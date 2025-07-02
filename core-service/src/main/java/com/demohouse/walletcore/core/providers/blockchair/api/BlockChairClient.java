package com.demohouse.walletcore.core.providers.blockchair.api;

import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairAddressContent;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTokenContent;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTransactionContent;
import com.demohouse.walletcore.core.providers.blockchair.api.models.responses.BlockChairAddressResponse;
import com.demohouse.walletcore.core.providers.blockchair.api.models.responses.BlockChairPushTransactionResponse;
import com.demohouse.walletcore.core.providers.blockchair.api.models.responses.BlockChairTokenResponse;
import com.demohouse.walletcore.core.providers.blockchair.api.models.responses.BlockChairTransactionResponse;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@Component
public class BlockChairClient {

    private final WebClient webClient;
    @Value("${wallet.crypto.api.blockchair.apiKey}")
    private String apiKey;

    public BlockChairClient() {
        this.webClient = WebClient.create();
    }

    private String getCoinName(Coin coin) {
        switch (coin) {
            case ETHEREUM:
            case TETHER:
                return "ethereum";
            case BITCOIN:
                return "bitcoin";
            case BITCOINSV:
                return "bitcoin-sv";
            case BITCOINCASH:
                return "bitcoin-cash";
            case RIPPLE:
                return "ripple";
            case LITECOIN:
                return "litecoin";
            case DOGECOIN:
                return "dogecoin";
            case DASH:
                return "dash";
            case ZCASH:
                return "zcash";
            default:
                return "";
        }
    }

    public BlockChairAddressContent getAddress(Coin coin, String address) {
        return getAddress(coin, address, false);
    }

    public BlockChairAddressContent getAddress(Coin coin, String address, boolean isErc20) {

        String coinName = getCoinName(coin);
        String uri = BlockChairConstants.getDashboardAddressUrl(coinName, address, apiKey);
        if (isErc20) uri = uri + "&erc_20=true";

        Mono<BlockChairAddressResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(BlockChairAddressResponse.class);

        return Objects.requireNonNull(bodyMono.block()).getData().get(address);
    }

    public BlockChairAddressContent getAddressConfirmedBalance(Coin coin, String address, boolean isErc20) {

        String coinName = getCoinName(coin);
        String uri = BlockChairConstants.getDashboardAddressUrl(coinName, address, apiKey) + "&state=latest";
        if (isErc20) uri = uri + "&erc_20=true";

        Mono<BlockChairAddressResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(BlockChairAddressResponse.class);

        return Objects.requireNonNull(bodyMono.block()).getData().get(address);
    }

    public BlockChairTransactionContent getTransaction(Coin coin, String transactionHash) {
        return getTransaction(coin, transactionHash, false);
    }

    public BlockChairTransactionContent getTransaction(Coin coin, String transactionHash, boolean isErc20) {
        String coinName = getCoinName(coin);
        String uri = BlockChairConstants.getDashboardTransactionUrl(coinName, transactionHash, apiKey);
        if (isErc20) uri = uri + "?erc_20=true";

        Mono<BlockChairTransactionResponse> bodyMono = webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(BlockChairTransactionResponse.class);

        return Objects.requireNonNull(bodyMono.block()).getData().get(transactionHash);
    }

    public BlockChairTokenContent getERC20TokenHolderDetails(String tokenAddress, String address) {
        Mono<BlockChairTokenResponse> bodyMono = webClient.get()
                .uri(BlockChairConstants.getErc20TokenHolderUri(tokenAddress, address, apiKey))
                .retrieve().bodyToMono(BlockChairTokenResponse.class);

        return Objects.requireNonNull(bodyMono.block()).getData().get(address);
    }

    public String pushTransaction(Coin coin, String txData) {

        String coinName = getCoinName(coin);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(BlockChairConstants.PUSH_TRANSACTION_DATA_KEY, txData);
        Mono<BlockChairPushTransactionResponse> bodyMono = webClient.post()
                .uri(BlockChairConstants.getPushTransactionUrl(coinName, apiKey))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve().bodyToMono(BlockChairPushTransactionResponse.class);

        return Objects.requireNonNull(bodyMono.block()).getData().getTransactionHash();
    }

}
