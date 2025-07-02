package com.demohouse.walletcore.core.providers.cryptonomic.helpers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.util.List;

public class CryptoNomicTezosHelper implements CryptoNomicHelper {

    @Override
    public Coin getCoin() {
        return Coin.TEZOS;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        return null;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        return null;
    }

    @Override
    public Boolean isConfirmed(String address) {
        return null;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        return null;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        return null;
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        return null;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return this.getCoin().convertValue(value);
    }
}
