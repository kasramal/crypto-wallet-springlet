package com.demohouse.walletcore.core.providers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class CryptoCurrencyApiServiceAdapter implements CryptoCurrencyApiService {

    private Date lockTime;

    @Autowired
    public List<ApiHelper> apiHelpers;

    @Override
    public ApiHelper getHelper(Coin coin) {
        for (ApiHelper apiHelper : apiHelpers) {
            if (apiHelper.getProvider().equals(this.getApiProvider())) {
                if (apiHelper.getCoin().equals(coin)) {
                    return apiHelper;
                }
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String privateKey, String from, String to, BigDecimal value, BigDecimal fee) {
        return this.pushTransaction(coin, privateKey, from, Collections.singletonList(new CryptoCurrencyPayment(to, value)), fee);
    }

    @Override
    public TransactionResult pushTransaction(Coin coin, String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        String txData = this.getHelper(coin).createTransaction(privateKey, from, payments, fee);

        TransactionResult transactionResult = this.pushTransaction(coin, txData);
        transactionResult.setFee(fee);
        return transactionResult;
    }

    @Override
    public boolean isLocked() {
        if (this.lockTime == null) return false;
        if (new Date().before(this.lockTime)) return true;
        this.lockTime = null;
        return false;
    }

    @Override
    public boolean isAccessible() {
        return !isLocked();
    }

    @Override
    public void lock() {
        Date lockTime = new Date(new Date().getTime() + getLockTimePeriod());
        this.setLockTime(lockTime);
    }

    @Override
    public Long getLockTimePeriod() {
        return 60L * 1000L;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }
}
