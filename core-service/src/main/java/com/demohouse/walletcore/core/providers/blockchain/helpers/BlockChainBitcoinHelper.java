package com.demohouse.walletcore.core.providers.blockchain.helpers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.utils.WalletHexUtils;
import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.blockexplorer.entity.Input;
import info.blockchain.api.blockexplorer.entity.Output;
import info.blockchain.api.blockexplorer.entity.Transaction;
import info.blockchain.api.blockexplorer.entity.UnspentOutput;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class BlockChainBitcoinHelper implements BlockChainHelper {

    private static final Coin COIN = Coin.BITCOIN;

    private final BlockExplorer blockExplorer;

    public BlockChainBitcoinHelper() {
        this.blockExplorer = new BlockExplorer();
    }

    private List<GenericUTXO> getUTXOs(String address) throws APIException, IOException {

        List<UnspentOutput> uTXOs = blockExplorer.getUnspentOutputs(address);
        List<GenericUTXO> genericUTXOS = new ArrayList<>();

        uTXOs.forEach(utxo -> genericUTXOS.add(new GenericUTXO(
                WalletHexUtils.littleEndianToBigEndian(utxo.getTransactionHash()),
                utxo.getN(),
                utxo.getScript(),
                address,
                BitcoinUtils.satoshiToBitcoin(new BigDecimal(utxo.getValue()))
        )));

        return genericUTXOS;
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS;
        try {
            genericUTXOS = getUTXOs(from);
        } catch (APIException | IOException ioException) {
            throw new RuntimeException(ioException);
        }
        GenericTransaction genericTransaction = new GenericTransactionBuilder().build(COIN, genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        try {
            return BigDecimal.valueOf(blockExplorer.getAddress(address).getFinalBalance());
        } catch (APIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean isConfirmed(String address) {
        List<UnspentOutput> unspentOutputs;

        try {
            unspentOutputs = blockExplorer.getUnspentOutputs(Collections.singletonList(address), 0, 20);
        } catch (APIException | IOException ioException) {
            throw new RuntimeException(ioException);
        }

        BigDecimal unconfirmedBalance = BigDecimal.ZERO;
        for (UnspentOutput output : unspentOutputs) {
            if (output.getConfirmations() <= 0)
                unconfirmedBalance = unconfirmedBalance.add(BitcoinUtils.satoshiToBitcoin(BigDecimal.valueOf(output.getValue())));
        }

        return unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0 && !unspentOutputs.isEmpty();
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        Transaction transaction;
        try {
            transaction = blockExplorer.getTransaction(transactionId);
        } catch (APIException | IOException ioException) {
            throw new RuntimeException(ioException);
        }

        return transaction != null && transaction.getBlockHeight() > 0;
    }

    @Override
    // isConfirmed would be set true after the very first confirmation
    public AccountStatus getTransactionStatus(String address) {
        List<UnspentOutput> unspentOutputs;

        try {
            unspentOutputs = blockExplorer.getUnspentOutputs(Collections.singletonList(address), 0, 20);
        } catch (APIException | IOException ioException) {
            throw new RuntimeException(ioException);
        }

        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal unconfirmedBalance = BigDecimal.ZERO;
        for (UnspentOutput output : unspentOutputs) {
            if (output.getConfirmations() > 0)
                balance = balance.add(BitcoinUtils.satoshiToBitcoin(BigDecimal.valueOf(output.getValue())));
            else
                unconfirmedBalance = unconfirmedBalance.add(BitcoinUtils.satoshiToBitcoin(BigDecimal.valueOf(output.getValue())));
        }

        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0 && !unspentOutputs.isEmpty();

        String transactionHash;
        try {
            transactionHash = blockExplorer.getAddress(address).getTransactions().get(0).getHash();
        } catch (APIException | IOException ioException) {
            throw new RuntimeException(ioException);
        }
        return new AccountStatus(isConfirmed, unconfirmedBalance, balance, transactionHash);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        Transaction transaction;
        try {
            transaction = blockExplorer.getTransaction(txHash);
        } catch (APIException | IOException ioException) {
            throw new RuntimeException(ioException);
        }
        validatePaymentDetails(transaction);

        BigDecimal fee = BigDecimal.ZERO;
        String buyerAddress = null;
        for (Input input : transaction.getInputs()) {
            buyerAddress = input.getPreviousOutput().getAddress();
            fee = fee.add(BigDecimal.valueOf(input.getPreviousOutput().getValue()));
        }
        BigDecimal receivedValue = BigDecimal.ZERO;
        for (Output output : transaction.getOutputs()) {
            fee = fee.subtract(BigDecimal.valueOf(output.getValue()));
            if (output.getAddress().equals(address))
                receivedValue = receivedValue.add(BigDecimal.valueOf(output.getValue()));
        }

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(BitcoinUtils.satoshiToBitcoin(receivedValue));
        paymentDetails.setSpentValue(BitcoinUtils.satoshiToBitcoin(receivedValue.add(fee)));
        paymentDetails.setTxId(txHash);
        paymentDetails.setFee(BitcoinUtils.satoshiToBitcoin(fee));
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    public BigDecimal processValue(BigDecimal value) {
        return BitcoinUtils.satoshiToBitcoin(value);
    }
}
