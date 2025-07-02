package com.demohouse.walletcore.core.providers.blockchair.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairErc20Token {

    private String tokenAddress;

    private String tokenName;

    private String tokenSymbol;

    private String tokenDecimals;

    private BigDecimal balanceApproximate;

    private BigDecimal balance;

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(String tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }

    public BigDecimal getBalanceApproximate() {
        return balanceApproximate;
    }

    public void setBalanceApproximate(BigDecimal balanceApproximate) {
        this.balanceApproximate = balanceApproximate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
