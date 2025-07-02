package com.demohouse.walletcore.core.transactions.coins.ethereum;

import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EthereumTransactionBuilder {

    public static EthereumTransaction build(EthereumAddress ethereumAddress, String to, BigDecimal value, BigDecimal fee) {
        EthereumTransaction ethereumTransaction = new EthereumTransaction();


        BigDecimal gasPrice = fee.divide(EthereumUtils.MIN_GAS_LIMIT, 14, RoundingMode.HALF_UP);
        if (ethereumAddress.getBalance().compareTo(value.add(fee)) < 0)
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INSUFFICIENT_BALANCE);

        ethereumTransaction.setAmount(value);
        ethereumTransaction.setNonce(ethereumAddress.getNonce());
        ethereumTransaction.setFee(fee);
        ethereumTransaction.setGasLimit(EthereumUtils.MIN_GAS_LIMIT);
        ethereumTransaction.setToAddress(to);
        ethereumTransaction.setGasPrice(gasPrice);
        ethereumTransaction.setData(new byte[0]);

        return ethereumTransaction;
    }
}
