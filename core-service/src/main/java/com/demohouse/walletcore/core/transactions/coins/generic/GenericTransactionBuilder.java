package com.demohouse.walletcore.core.transactions.coins.generic;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinTransaction;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashTransaction;
import com.demohouse.walletcore.core.transactions.coins.bitcoinsv.BitcoinSVTransaction;
import com.demohouse.walletcore.core.transactions.coins.dash.DashTransaction;
import com.demohouse.walletcore.core.transactions.coins.dogecoin.DogecoinTransaction;
import com.demohouse.walletcore.core.transactions.coins.litecoin.LitecoinTransaction;
import com.demohouse.walletcore.core.transactions.coins.zcash.ZcashTransaction;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.utils.WalletHexUtils;

import java.math.BigDecimal;
import java.util.List;

public class GenericTransactionBuilder {


    public String generatePublicKeyScript(String address) {
        return GenericUtils.generatePublicKeyScript(address);
    }

    public GenericTransaction build(Coin coin, List<GenericUTXO> utxos, String from, String to, BigDecimal value, BigDecimal fee) {
        GenericTransaction genericTransaction = factory(coin);

        BigDecimal uTXOsSumValue = createTransactionInputs(utxos, genericTransaction, value.add(fee));

        GenericTransactionOutput outputTo = new GenericTransactionOutput();
        String toAddressScriptPubKey = generatePublicKeyScript(to);
        outputTo.setPubKeyScript(toAddressScriptPubKey);
        outputTo.setValue(value.subtract(fee));
        genericTransaction.addTransactionOutput(outputTo);

        validateBalanceSufficiency(uTXOsSumValue, value);

        BigDecimal uTXOsRemainingValue = uTXOsSumValue.subtract(value);
        appendChangeValueToTransactionOutputs(from, value, genericTransaction, uTXOsRemainingValue);

        genericTransaction.setFromAddress(from);
        genericTransaction.setToAddress(to);
        return genericTransaction;
    }

    private GenericTransaction factory(Coin coin) {
        GenericTransaction genericTransaction;
        switch (coin) {
            case BITCOIN:
                genericTransaction = new BitcoinTransaction();
                break;
            case BITCOINCASH:
                genericTransaction = new BitcoinCashTransaction();
                break;
            case BITCOINSV:
                genericTransaction = new BitcoinSVTransaction();
                break;
            case ZCASH:
                genericTransaction = new ZcashTransaction();
                break;
            case DASH:
                genericTransaction = new DashTransaction();
                break;
            case LITECOIN:
                genericTransaction = new LitecoinTransaction();
                break;
            case DOGECOIN:
                genericTransaction = new DogecoinTransaction();
                break;
            default:
                throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
        }
        return genericTransaction;
    }

    public GenericTransaction build(Coin coin, List<GenericUTXO> utxos, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        GenericTransaction genericTransaction = factory(coin);


        BigDecimal paymentValues = createTransactionOutputs(payments, genericTransaction);

        BigDecimal uTXOsSumValue = createTransactionInputs(utxos, genericTransaction, paymentValues.add(fee));


        validateBalanceSufficiency(uTXOsSumValue, paymentValues.add(fee));

        BigDecimal uTXOsRemainingValue = uTXOsSumValue.subtract(paymentValues);
        appendChangeValueToTransactionOutputs(from, fee, genericTransaction, uTXOsRemainingValue);

        genericTransaction.setFromAddress(from);
        return genericTransaction;
    }

    private void validateBalanceSufficiency(BigDecimal uTXOsSumValue, BigDecimal value) {
        if (uTXOsSumValue.compareTo(value) < 0)
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INSUFFICIENT_BALANCE);
    }

    private BigDecimal createTransactionInputs(List<GenericUTXO> utxos, GenericTransaction genericTransaction, BigDecimal value) {
        BigDecimal uTXOsSumValue = BigDecimal.ZERO;
        for (GenericUTXO unspentOutput : utxos) {
            if (uTXOsSumValue.compareTo(value) > 0)
                break;
            GenericTransactionInput input = new GenericTransactionInput();
            input.setHashLittleEndian(WalletHexUtils.hexStringToLittleEndian(unspentOutput.getTransactionHash()));
            input.setIndex(unspentOutput.getTransactionIndex());
            input.setPubKeyScript(unspentOutput.getScript());
            input.setIndex(unspentOutput.getTransactionIndex());
            input.setValue(unspentOutput.getValue());
            genericTransaction.addTransactionInput(input);
            uTXOsSumValue = uTXOsSumValue.add(unspentOutput.getValue());
        }
        return uTXOsSumValue;
    }

    private BigDecimal createTransactionOutputs(List<CryptoCurrencyPayment> payments, GenericTransaction genericTransaction) {
        BigDecimal paymentValues = BigDecimal.ZERO;
        for (CryptoCurrencyPayment payment : payments) {
            GenericTransactionOutput outputTo = new GenericTransactionOutput();
            String toAddressScriptPubKey = generatePublicKeyScript(payment.getTo());
            outputTo.setPubKeyScript(toAddressScriptPubKey);
            outputTo.setValue(payment.getValue());
            genericTransaction.addTransactionOutput(outputTo);
            paymentValues = paymentValues.add(payment.getValue());
        }
        return paymentValues;
    }

    private void appendChangeValueToTransactionOutputs(String from, BigDecimal fee, GenericTransaction genericTransaction, BigDecimal uTXOsRemainingValue) {
        if (uTXOsRemainingValue.subtract(fee).compareTo(BigDecimal.ZERO) != 0) {
            GenericTransactionOutput outputChange = new GenericTransactionOutput();
            String fromAddressScriptPubKey = generatePublicKeyScript(from);
            outputChange.setPubKeyScript(fromAddressScriptPubKey);
            outputChange.setValue(uTXOsRemainingValue.subtract(fee));
            genericTransaction.addTransactionOutput(outputChange);
        }
    }
}
