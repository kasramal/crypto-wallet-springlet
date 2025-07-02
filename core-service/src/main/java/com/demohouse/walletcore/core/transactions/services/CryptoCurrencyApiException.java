package com.demohouse.walletcore.core.transactions.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "API EXCEPTION.")
public class CryptoCurrencyApiException extends RuntimeException {

    CryptoCurrencyError error;
    String args[];

    public CryptoCurrencyApiException(CryptoCurrencyError error) {
        super(error.getMessageKey());
        this.error = error;
    }

    public CryptoCurrencyApiException(CryptoCurrencyError error, String[] args) {
        super(error.getMessageKey());
        this.error = error;
        this.args = args;
    }

    public CryptoCurrencyError getError() {
        return error;
    }

    public void setError(CryptoCurrencyError error) {
        this.error = error;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
