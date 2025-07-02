package com.demohouse.walletcore.core.providers.blockcypher.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockCypherAddress {
    private BigDecimal balance;
    private BigDecimal nonce;
    private BigDecimal unconfirmedBalance;
    private BigDecimal unconfirmedNTx;
    private List<BlockCypherTransaction> txs;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getNonce() {
        return nonce;
    }

    public void setNonce(BigDecimal nonce) {
        this.nonce = nonce;
    }

    public BigDecimal getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public void setUnconfirmedBalance(BigDecimal unconfirmedBalance) {
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public BigDecimal getUnconfirmedNTx() {
        return unconfirmedNTx;
    }

    public void setUnconfirmedNTx(BigDecimal unconfirmedNTx) {
        this.unconfirmedNTx = unconfirmedNTx;
    }

    public List<BlockCypherTransaction> getTxs() {
        return txs;
    }

    public void setTxs(List<BlockCypherTransaction> txs) {
        this.txs = txs;
    }
}
