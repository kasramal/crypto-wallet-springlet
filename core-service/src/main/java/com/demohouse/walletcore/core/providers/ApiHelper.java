package com.demohouse.walletcore.core.providers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.util.List;

public interface ApiHelper {

    CryptoCurrencyApiProvider getProvider();

    Coin getCoin();

    String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee);

    BigDecimal getAddressBalance(String address);

    Boolean isConfirmed(String address);

    Boolean isConfirmedByTransactionId(String transactionId);

    AccountStatus getTransactionStatus(String address);

    PaymentDetails getTransactionPaymentDetails(String txHash, String address);

    BigDecimal processValue(BigDecimal value);

    default void validatePaymentDetails(Object transaction) {
        if (transaction == null) {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.TRANSACTION_NOT_CONFIRMED_YET);
        }
    }
}
