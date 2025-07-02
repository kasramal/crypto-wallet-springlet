package com.demohouse.walletcore.core.providers.cryptoapis.api.models;

import java.math.BigDecimal;
import java.util.List;

public class CryptoApisTransactionOutput {

    private BigDecimal amount;

    private Boolean spent;

    private CryptoApisScript script;

    private List<String> addresses;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getSpent() {
        return spent;
    }

    public void setSpent(Boolean spent) {
        this.spent = spent;
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
