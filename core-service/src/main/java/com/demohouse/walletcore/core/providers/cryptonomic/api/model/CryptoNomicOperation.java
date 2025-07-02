package com.demohouse.walletcore.core.providers.cryptonomic.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptoNomicOperation implements Serializable {

    private String kind;
    private String source;
    private String fee;
    private String counter;
    private String gasLimit;
    private String storageLimit;
    private String publicKey;
    private String amount;
    private String destination;
    private CryptoNomicOperationMetaData metadata;

    public CryptoNomicOperation() {
    }

    public CryptoNomicOperation(String kind, String source, String fee, String counter, String gasLimit, String storageLimit, String publicKey, String amount, String destination) {
        this.kind = kind;
        this.source = source;
        this.fee = fee;
        this.counter = counter;
        this.gasLimit = gasLimit;
        this.storageLimit = storageLimit;
        this.publicKey = publicKey;
        this.amount = amount;
        this.destination = destination;
    }

    public static class Builder {

        public static class Reveal {
            private String source;
            private String counter;
            private String publicKey;


            public Reveal source(String source) {
                this.source = source;
                return this;
            }

            public Reveal counter(String counter) {
                this.counter = counter;
                return this;
            }

            public Reveal publicKey(String publicKey) {
                this.publicKey = publicKey;
                return this;
            }

            public CryptoNomicOperation build() {
                return new CryptoNomicOperation("reveal", source, "0", counter, "10000", "0", publicKey, null, null);
            }
        }

        public static class Transaction {
            private String source;
            private String fee;
            private String counter;
            private String gasLimit;
            private String storageLimit;
            private String amount;
            private String destination;

            public Transaction source(String source) {
                this.source = source;
                return this;
            }

            public Transaction fee(String fee) {
                this.fee = fee;
                return this;
            }

            public Transaction counter(String counter) {
                this.counter = counter;
                return this;
            }

            public Transaction gasLimit(String gasLimit) {
                this.gasLimit = gasLimit;
                return this;
            }

            public Transaction storageLimit(String storageLimit) {
                this.storageLimit = storageLimit;
                return this;
            }

            public Transaction amount(String amount) {
                this.amount = amount;
                return this;
            }

            public Transaction destination(String destination) {
                this.destination = destination;
                return this;
            }

            public CryptoNomicOperation build() {
                return new CryptoNomicOperation("transaction", source, fee, counter, gasLimit, storageLimit, null, amount, destination);
            }
        }

    }
}
