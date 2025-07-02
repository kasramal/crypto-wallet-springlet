package com.demohouse.walletcore.core.transactions.coins.ethereum.erc20;

import java.math.BigDecimal;

public class Erc20Token {

    private String tokenSymbol;
    private String tokenAddress;
    private String txHashId;
    private BigDecimal balance;


    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getTxHashId() {
        return txHashId;
    }

    public void setTxHashId(String txHashId) {
        this.txHashId = txHashId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
