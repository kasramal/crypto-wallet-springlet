package com.demohouse.walletcore.core.providers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoCurrencyApiService {

    ApiHelper getHelper(Coin coin);

    CryptoCurrencyApiProvider getApiProvider();

    TransactionResult pushTransaction(Coin coin, String privateKey, String from, String to, BigDecimal value, BigDecimal fee);

    TransactionResult pushTransaction(Coin coin, String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee);

    TransactionResult pushTransaction(Coin coin, String transactionHex);

    Long getLockTimePeriod();

    boolean isLocked();

    boolean isAccessible();

    void lock();
}
