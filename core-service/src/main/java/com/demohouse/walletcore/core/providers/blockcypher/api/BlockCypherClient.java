package com.demohouse.walletcore.core.providers.blockcypher.api;

import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherAddress;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherTransaction;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.requests.BlockCypherPushTransactionRequest;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.responses.BlockCypherTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BlockCypherClient {

    private final WebClient webClient;
    @Value("${wallet.crypto.api.blockcypher.version}")
    private String BLOCK_CYPHER_VERSION;
    @Value("${wallet.crypto.api.blockcypher.network}")
    private String BLOCK_CYPHER_NETWORK;
    @Value("${wallet.crypto.api.blockcypher.token}")
    private String BLOCK_CYPHER_TOKEN;

    public BlockCypherClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private String getBlockCypherCoin(Coin coin) {
        switch (coin) {
            case BITCOIN: return "btc";
            case DOGECOIN: return "doge";
            case LITECOIN: return "ltc";
            case TETHER:
            case ETHEREUM: return "eth";
        }
        return "";
    }

    public BlockCypherAddress getAddress(Coin coin, String address) {
        Mono<BlockCypherAddress> bodyMono = webClient.get()
                .uri(BlockCypherConstants.getAddressFullURL(
                        address,
                        BLOCK_CYPHER_VERSION,
                        getBlockCypherCoin(coin) ,
                        BLOCK_CYPHER_NETWORK))
                .retrieve().bodyToMono(BlockCypherAddress.class);

        return bodyMono.block();
    }

    public BlockCypherTransaction getTransaction(Coin coin, String transactionHash) {
        Mono<BlockCypherTransaction> bodyMono = webClient.get()
                .uri(BlockCypherConstants.getTransactionURL(
                        transactionHash,
                        BLOCK_CYPHER_VERSION,
                        getBlockCypherCoin(coin) ,
                        BLOCK_CYPHER_NETWORK))
                .retrieve().bodyToMono(BlockCypherTransaction.class);

        return bodyMono.block();
    }

    public BlockCypherTransaction pushTransaction(Coin coin, String txData) {
        BlockCypherPushTransactionRequest request = new BlockCypherPushTransactionRequest(txData);
        Mono<BlockCypherTransactionResponse> bodyMono = webClient.post()
                .uri(BlockCypherConstants.getPushTransactionURL(
                        BLOCK_CYPHER_VERSION,
                        getBlockCypherCoin(coin) ,
                        BLOCK_CYPHER_NETWORK,
                        BLOCK_CYPHER_TOKEN))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(BlockCypherTransactionResponse.class);

        return bodyMono.block().getTx();
    }
}
