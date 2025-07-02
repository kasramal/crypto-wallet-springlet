package com.demohouse.walletcore.core.manager;

import com.demohouse.walletcore.core.addresses.AddressValidatorFactory;
import com.demohouse.walletcore.core.manager.util.RunnerUtils;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiService;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.TransactionValidatorFactory;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.outputs.TransactionResult;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CoinManager {

    Coin getCoin();

    int getMaxRetries();

    TransactionValidatorFactory getTransactionValidatorFactory();

    List<CryptoCurrencyApiProvider> getPushProviders();

    List<CryptoCurrencyApiProvider> getExplorerProviders();

    List<CryptoCurrencyApiService> getPushServices();

    List<CryptoCurrencyApiService> getExplorerServices();

    default TransactionResult pushTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee) {
        TransactionResult transactionResult;
        this.getTransactionValidatorFactory().validate(this.getCoin(), from, to, value, fee);
        for (CryptoCurrencyApiService pushService : this.getPushServices()) {
            System.out.println("Using " + pushService.getApiProvider() + " to pushTransaction");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                transactionResult = RunnerUtils.pushApiCallRunner(
                        pushService,
                        () -> pushService.pushTransaction(this.getCoin(), privateKey, from, to, value, fee));
                if (transactionResult != null) return transactionResult;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default TransactionResult pushTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        TransactionResult transactionResult;
        this.getTransactionValidatorFactory().validate(this.getCoin(), from, payments, fee);
        for (CryptoCurrencyApiService pushService : this.getPushServices()) {
            System.out.println("Using " + pushService.getApiProvider() + " to pushTransaction");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                transactionResult = RunnerUtils.pushApiCallRunner(
                        pushService,
                        () -> pushService.pushTransaction(this.getCoin(), privateKey, from, payments, fee));
                if (transactionResult != null) return transactionResult;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default TransactionResult pushTransaction(String transactionHex) {
        TransactionResult transactionResult;
        for (CryptoCurrencyApiService pushService : this.getPushServices()) {
            System.out.println("Using " + pushService.getApiProvider() + " to pushTransaction");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                transactionResult = RunnerUtils.pushApiCallRunner(
                        pushService,
                        () -> pushService.pushTransaction(this.getCoin(), transactionHex));
                if (transactionResult != null) return transactionResult;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default String createTransactions(String privateKey, String from, String to, BigDecimal value, BigDecimal fee) {
        String result;
        for (CryptoCurrencyApiService pushService : this.getPushServices()) {
            System.out.println("Using " + pushService.getApiProvider() + " to createTransaction");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                result = RunnerUtils.pushApiCallRunner(
                        pushService,
                        () -> pushService.getHelper(this.getCoin()).createTransaction(privateKey, from, Collections.singletonList(new CryptoCurrencyPayment(to, value)), fee));
                if (result != null) return result;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default BigDecimal getAddressBalance(String address) {
        BigDecimal result;
        for (CryptoCurrencyApiService explorerService : this.getExplorerServices()) {
            System.out.println("Using " + explorerService.getApiProvider() + " for getAddressBalance");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                result = RunnerUtils.apiCallRunner(
                        explorerService,
                        () -> explorerService.getHelper(this.getCoin()).getAddressBalance(address));
                if (result != null) return result;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default Boolean isConfirmed(String address) {
        Boolean result;
        for (CryptoCurrencyApiService explorerService : this.getExplorerServices()) {
            System.out.println("Using " + explorerService.getApiProvider() + " for isConfirmed");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                result = RunnerUtils.apiCallRunner(
                        explorerService,
                        () -> explorerService.getHelper(this.getCoin()).isConfirmed(address));
                if (result != null) return result;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default Boolean isConfirmedByTransactionId(String transactionId) {
        Boolean result;
        for (CryptoCurrencyApiService explorerService : this.getExplorerServices()) {
            System.out.println("Using " + explorerService.getApiProvider() + " for isConfirmed");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                result = RunnerUtils.apiCallRunner(
                        explorerService,
                        () -> explorerService.getHelper(this.getCoin()).isConfirmedByTransactionId(transactionId));
                if (result != null) return result;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default AccountStatus getTransactionStatus(String address) {
        AccountStatus result;
        AddressValidatorFactory.validateAddress(this.getCoin(), address);
        for (CryptoCurrencyApiService explorerService : this.getExplorerServices()) {
            System.out.println("Using " + explorerService.getApiProvider() + " to getTransactionStatus");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                result = RunnerUtils.apiCallRunner(
                        explorerService,
                        () -> explorerService.getHelper(this.getCoin()).getTransactionStatus(address));
                if (result != null) return result;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    default Map<CryptoCurrencyApiProvider, AccountStatus> getTransactionStatusMap(String address) {
        AccountStatus result;
        Map<CryptoCurrencyApiProvider, AccountStatus> map = new HashMap<>();
        AddressValidatorFactory.validateAddress(this.getCoin(), address);
        for (CryptoCurrencyApiService explorerService : this.getExplorerServices()) {
            System.out.println("Using " + explorerService.getApiProvider() + " to getTransactionStatus");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                result = RunnerUtils.apiCallRunner(
                        explorerService,
                        () -> explorerService.getHelper(this.getCoin()).getTransactionStatus(address));
                map.put(explorerService.getApiProvider(), result);
            }
        }
        return map;
    }

    default PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        PaymentDetails result;
        AddressValidatorFactory.validateAddress(this.getCoin(), address);
        for (CryptoCurrencyApiService explorerService : this.getExplorerServices()) {
            System.out.println("Using " + explorerService.getApiProvider() + " to getTransactionPaymentDetails");
            for (int i = 0; i < this.getMaxRetries(); i++) {
                result = RunnerUtils.apiCallRunner(
                        explorerService,
                        () -> explorerService.getHelper(this.getCoin()).getTransactionPaymentDetails(txHash, address));
                if (result != null) return result;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }
}
