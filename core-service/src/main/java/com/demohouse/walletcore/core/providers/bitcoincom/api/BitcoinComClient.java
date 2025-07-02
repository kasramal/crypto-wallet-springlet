package com.demohouse.walletcore.core.providers.bitcoincom.api;

import com.demohouse.walletcore.core.providers.bitcoincom.api.model.response.BitcoinComAccountBalanceResponse;
import com.demohouse.walletcore.core.providers.bitcoincom.api.model.response.BitcoinComAccountUTXOsResponse;
import com.demohouse.walletcore.core.providers.bitcoincom.api.model.response.BitcoinComTransactionPayloadResponse;
import com.demohouse.walletcore.core.providers.bitcoincom.api.model.response.BitcoinComTransactionResponse;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class BitcoinComClient {

    private final WebClient webClient;

    public BitcoinComClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public BitcoinComAccountBalanceResponse getAccountBalance(String address) {
        BitcoinComAccountBalanceResponse response = webClient
                .get()
                .uri(BitcoinComConstants.getAccountBalanceURL(address))
                .retrieve()
                .bodyToMono(BitcoinComAccountBalanceResponse.class)
                .block();
        if (response != null) {
            return response;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public BitcoinComAccountUTXOsResponse getAccountUTXOs(String address) {
        BitcoinComAccountUTXOsResponse response = webClient
                .get()
                .uri(BitcoinComConstants.getAccountUTXOsURL(address))
                .retrieve()
                .bodyToMono(BitcoinComAccountUTXOsResponse.class)
                .block();
        if (response != null) {
            return response;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public List<BitcoinComTransactionResponse> getTransactionsByAddress(String address) {
        BitcoinComTransactionPayloadResponse response = webClient
                .get()
                .uri(BitcoinComConstants.getTransactionByAddressURL(address))
                .retrieve()
                .bodyToMono(BitcoinComTransactionPayloadResponse.class)
                .block();
        if (response != null) {
            return response.getTransactions();
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public BitcoinComTransactionResponse getTransactionByHash(String txHash) {
        BitcoinComTransactionResponse response = webClient
                .get()
                .uri(BitcoinComConstants.getTransactionByHashURL(txHash))
                .retrieve()
                .bodyToMono(BitcoinComTransactionResponse.class)
                .block();
        if (response != null) {
            return response;
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    public String pushTransaction(String txHex) {
        String response = webClient
                .get()
                .uri(BitcoinComConstants.getPushTransactionURL(txHex))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (response != null) {
            return response.replace("\"", "").replace("\n", "");
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }
}
