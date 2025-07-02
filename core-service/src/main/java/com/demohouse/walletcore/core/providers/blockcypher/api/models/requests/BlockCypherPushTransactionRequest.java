package com.demohouse.walletcore.core.providers.blockcypher.api.models.requests;

public class BlockCypherPushTransactionRequest {
    private String tx;

    public BlockCypherPushTransactionRequest(String tx) {
        this.tx = tx;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }
}
