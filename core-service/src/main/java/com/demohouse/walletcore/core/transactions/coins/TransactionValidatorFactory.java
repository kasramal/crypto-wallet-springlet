package com.demohouse.walletcore.core.transactions.coins;

import com.demohouse.walletcore.configuration.properties.CoinTransactionProperties;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionValidatorFactory {

    private final Map<Coin, TransactionValidator> transactionValidators = new HashMap<>();
    private final CoinTransactionProperties coinTransactionProperties;

    public TransactionValidatorFactory(List<TransactionValidator> transactionValidators,
                                       CoinTransactionProperties coinTransactionProperties) {
        this.coinTransactionProperties = coinTransactionProperties;
        for (TransactionValidator transactionValidator : transactionValidators) {
            this.transactionValidators.put(transactionValidator.getCoin(), transactionValidator);
        }
    }

    public TransactionValidator getTransactionValidator(Coin coin) {
        TransactionValidator transactionValidator = this.transactionValidators.get(coin);
        if (transactionValidator != null) {
            return transactionValidator;
        } else throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

    public void validate(Coin coin, String from, String to, BigDecimal value, BigDecimal fee) {
        this.getTransactionValidator(coin).validate(fee, value, from, to, this.getMinValue(coin));
    }

    public void validate(Coin coin, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        this.getTransactionValidator(coin).validate(from, payments, this.getMinValue(coin));
    }

    private BigDecimal getMinValue(Coin coin) {
        Map<String, CoinTransactionProperties.Value> coins = coinTransactionProperties.getCoins();
        CoinTransactionProperties.Value value = coins.get(coin.getName());
        if (value != null) {
            return coin.convertValue(BigDecimal.valueOf(value.getMinValue()));
        } else throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

}
