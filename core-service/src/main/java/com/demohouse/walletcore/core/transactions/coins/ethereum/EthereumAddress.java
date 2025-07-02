package com.demohouse.walletcore.core.transactions.coins.ethereum;

import java.math.BigDecimal;

public class EthereumAddress {

    private BigDecimal nonce;
    private BigDecimal balance;

    public BigDecimal getNonce() {
        return nonce;
    }

    public void setNonce(BigDecimal nonce) {
        this.nonce = nonce;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
