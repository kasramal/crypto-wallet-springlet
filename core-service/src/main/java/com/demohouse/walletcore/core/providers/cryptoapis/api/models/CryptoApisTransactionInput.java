package com.demohouse.walletcore.core.providers.cryptoapis.api.models;

import java.math.BigDecimal;
import java.util.List;

public class CryptoApisTransactionInput {

    private String txout;

    private Integer vout;

    private BigDecimal amount;

    private CryptoApisScript script;

    private List<String> addresses;

    public String getTxout() {
        return txout;
    }

    public void setTxout(String txout) {
        this.txout = txout;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CryptoApisScript getScript() {
        return script;
    }

    public void setScript(CryptoApisScript script) {
        this.script = script;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
