package com.demohouse.walletcore.core.providers.blockchair.helpers;

import com.demohouse.walletcore.core.providers.BalanceCalculationMode;
import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairConstants;
import com.demohouse.walletcore.core.providers.blockchair.api.models.*;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockChairGenericHelper implements BlockChairHelper {


    private final BlockChairClient blockChairClient;

    public BlockChairGenericHelper(BlockChairClient blockChairClient) {
        this.blockChairClient = blockChairClient;
    }

    public String generatePublicKeyScript(String address) {
        return GenericUtils.generatePublicKeyScript(address);
    }

    public List<GenericUTXO> getUTXOs(String address) {
        List<BlockChairUTXO> uTXOs = blockChairClient.getAddress(getCoin(), address).getUtxo();
        List<GenericUTXO> genericUTXOS = new ArrayList<>();
        String scriptPublicKey = generatePublicKeyScript(address);
        uTXOs.forEach(utxo -> genericUTXOS.add(new GenericUTXO(
                utxo.getTransactionHash(),
                utxo.getIndex(),
                scriptPublicKey,
                address,
                processValue(utxo.getValue())
        )));
        return genericUTXOS;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new GenericTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        BlockChairAddressContent blockChairConfirmedContent = blockChairClient.getAddressConfirmedBalance(getCoin(), address, false);
        BigDecimal balance = blockChairConfirmedContent.getAddress().getBalance();
        return processValue(balance);
    }

    @Override
    public Boolean isConfirmed(String address) {

        BlockChairAddressContent blockChairAddressContent = blockChairClient.getAddress(getCoin(), address);
        BlockChairAddressContent blockChairConfirmedContent = blockChairClient.getAddressConfirmedBalance(getCoin(), address, false);

        BigDecimal balance = blockChairConfirmedContent.getAddress().getBalance();
        BigDecimal unconfirmedBalance = blockChairAddressContent.getAddress().getBalance().subtract(balance);

        return unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        BlockChairTransaction transaction = blockChairClient.getTransaction(getCoin(), transactionId).getTransaction();
        return transaction != null && transaction.getBlockId() != BlockChairConstants.UNCONFIRMED_BLOCK_ID;
    }

    @Override
    // isConfirmed would be set true after the very first confirmation
    public AccountStatus getTransactionStatus(String address) {
        BlockChairAddressContent blockChairAddressContent = blockChairClient.getAddress(getCoin(), address);
        BlockChairAddressContent blockChairConfirmedContent = blockChairClient.getAddressConfirmedBalance(getCoin(), address, false);

        BigDecimal balance = blockChairConfirmedContent.getAddress().getBalance();
        BigDecimal unconfirmedBalance = blockChairAddressContent.getAddress().getBalance().subtract(balance);

        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;
        String transactionHash = null;
        if (!blockChairAddressContent.getTransactions().isEmpty())
            transactionHash = blockChairAddressContent.getTransactions().get(0);
        return new AccountStatus(
                isConfirmed,
                processValue(unconfirmedBalance),
                processValue(balance),
                transactionHash,
                BalanceCalculationMode.TRANSACTION_BASED_BALANCE_CALCULATION);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BlockChairTransactionContent tx = blockChairClient.getTransaction(getCoin(), txHash);
        validatePaymentDetails(tx);
        BigDecimal fee = tx.getTransaction().getFee();
        BigDecimal receivedValue = BigDecimal.ZERO;
        for (BlockChairTransactionOutput output : tx.getOutputs()) {
            if (output.getRecipient().equals(address))
                receivedValue = receivedValue.add(output.getValue());
        }
        String buyerAddress = null;
        for (BlockChairTransactionInput input : tx.getInputs())
            buyerAddress = input.getRecipient();


        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue.add(fee)));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    public BigDecimal processValue(BigDecimal value) {
        return getCoin().convertValue(value);
    }
}
