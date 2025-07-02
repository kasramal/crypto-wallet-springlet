package com.demohouse.walletcore.core.transactions.coins.ethereum.erc20;

import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumAddress;

import java.util.List;

public class Erc20Address extends EthereumAddress {

    private String tokenSymbol;
    private List<Erc20Token> tokens;


    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public List<Erc20Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Erc20Token> tokens) {
        this.tokens = tokens;
    }
}
