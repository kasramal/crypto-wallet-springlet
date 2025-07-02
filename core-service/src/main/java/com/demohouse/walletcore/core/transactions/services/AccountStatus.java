package com.demohouse.walletcore.core.transactions.services;

import com.demohouse.walletcore.core.providers.BalanceCalculationMode;

import java.math.BigDecimal;

public class AccountStatus {

    public static AccountStatus UNCONFIRMED = new AccountStatus(false, BigDecimal.valueOf(0), "");
    private Boolean confirmed;
    private BigDecimal unconfirmedBalance;
    private BigDecimal balance;
    private String transactionHash;
    private String tokenAddress;
    private BalanceCalculationMode calculationMode;

    public AccountStatus(Boolean confirmed, BigDecimal balance, String hash) {
        this.confirmed = confirmed;
        this.balance = balance;
        this.transactionHash = hash;
        this.tokenAddress = hash;
        this.calculationMode = BalanceCalculationMode.CONFIRMED_BALANCE_CALCULATION;
    }

    public AccountStatus(Boolean confirmed, BigDecimal unconfirmedBalance, BigDecimal balance, String hash) {
        this.confirmed = confirmed;
        this.balance = balance;
        this.unconfirmedBalance = unconfirmedBalance;
        this.transactionHash = hash;
        this.tokenAddress = hash;
        this.calculationMode = BalanceCalculationMode.TRANSACTION_BASED_BALANCE_CALCULATION;
    }

    public AccountStatus(Boolean confirmed, BigDecimal unconfirmedBalance, BigDecimal balance, String hash, BalanceCalculationMode calculationMode) {
        this.confirmed = confirmed;
        this.balance = balance;
        this.unconfirmedBalance = unconfirmedBalance;
        this.transactionHash = hash;
        this.tokenAddress = hash;
        this.calculationMode = calculationMode;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public BigDecimal getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public void setUnconfirmedBalance(BigDecimal unconfirmedBalance) {
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public BalanceCalculationMode getCalculationMode() {
        return calculationMode;
    }

    public void setCalculationMode(BalanceCalculationMode calculationMode) {
        this.calculationMode = calculationMode;
    }
}
