package com.demohouse.walletcore.core.transactions.coins;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionValidator extends AbstractCoin{

    boolean validate(BigDecimal fee, BigDecimal value, String to, String from, BigDecimal minValue);

    boolean validate(String from, List<CryptoCurrencyPayment> payments, BigDecimal minValue);
}
