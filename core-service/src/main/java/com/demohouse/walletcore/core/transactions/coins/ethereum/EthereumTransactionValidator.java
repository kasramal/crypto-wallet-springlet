package com.demohouse.walletcore.core.transactions.coins.ethereum;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.TransactionValidator;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class EthereumTransactionValidator implements TransactionValidator {

    @Override
    public boolean validate(BigDecimal fee, BigDecimal value, String to, String from, BigDecimal minValue) {
        if (value.compareTo(fee) < 0)
            throw new CryptoCurrencyApiException(CryptoCurrencyError.TRANSACTION_VALUE_LESS_THAN_FEE);
        if (value.subtract(fee).compareTo(minValue) < 0)
            throw new CryptoCurrencyApiException(CryptoCurrencyError.MIN_VALUE_VIOLATED);
        if (!EthereumUtils.isAddressValid(to))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
        if (!EthereumUtils.isAddressValid(from))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
        return true;
    }

    @Override
    public boolean validate(String from, List<CryptoCurrencyPayment> payments, BigDecimal minValue) {
        if (!EthereumUtils.isAddressValid(from))
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
        for (CryptoCurrencyPayment payment : payments) {
            if (payment.getValue().compareTo(minValue) < 0)
                throw new CryptoCurrencyApiException(CryptoCurrencyError.MIN_VALUE_VIOLATED);
            if (!EthereumUtils.isAddressValid(payment.getTo()))
                throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
        }
        return true;
    }

    @Override
    public Coin getCoin() {
        return Coin.ETHEREUM;
    }
}
