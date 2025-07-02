package com.demohouse.walletcore.core.providers.blockchair.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlockChairTransaction {

    private Integer blockId;

    private String hash;

    private String transactionHash;

    private BigDecimal value;

    private String sender;

    private BigDecimal inputTotal;

    private BigDecimal outputTotal;

    private BigDecimal fee;

    private String tokenAddress;

    private String tokenName;

    private String tokenSymbol;

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public BigDecimal getInputTotal() {
        return inputTotal;
    }

    public void setInputTotal(BigDecimal inputTotal) {
        this.inputTotal = inputTotal;
    }

    public BigDecimal getOutputTotal() {
        return outputTotal;
    }

    public void setOutputTotal(BigDecimal outputTotal) {
        this.outputTotal = outputTotal;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }
}
