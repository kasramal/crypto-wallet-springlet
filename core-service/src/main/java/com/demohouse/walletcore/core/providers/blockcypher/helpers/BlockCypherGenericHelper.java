package com.demohouse.walletcore.core.providers.blockcypher.helpers;

import com.demohouse.walletcore.core.providers.blockcypher.api.BlockCypherClient;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherAddress;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherTransaction;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherTransactionOutput;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockCypherGenericHelper implements BlockCypherHelper {


    private final BlockCypherClient blockCypherClient;

    public BlockCypherGenericHelper(BlockCypherClient blockCypherClient) {
        this.blockCypherClient = blockCypherClient;
    }

    public String generatePublicKeyScript(String address) {
        return GenericUtils.generatePublicKeyScript(address);
    }

    private List<GenericUTXO> getUTXOs(String address) {

        BlockCypherAddress blockCypherAddress = blockCypherClient.getAddress(getCoin(), address);
        List<GenericUTXO> genericUTXOS = new ArrayList<>();

        for (BlockCypherTransaction transaction : blockCypherAddress.getTxs()) {

            for (int index = 0; index < transaction.getOutputs().size(); index++) {
                BlockCypherTransactionOutput output = transaction.getOutputs().get(index);
                if (address.equals(output.getAddresses().get(0)) && output.getSpentBy() == null) {
                    genericUTXOS.add(new GenericUTXO(
                            transaction.getHash(),
                            index,
                            output.getScript(),
                            address,
                            BitcoinUtils.satoshiToBitcoin(output.getValue())));
                }
            }
        }
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
        return this.processValue(blockCypherClient.getAddress(getCoin(), address).getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        BlockCypherAddress blockCypherAddress = blockCypherClient.getAddress(getCoin(), address);
        return blockCypherAddress.getUnconfirmedNTx().longValue() == 0;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        BlockCypherTransaction transaction = this.blockCypherClient.getTransaction(this.getCoin(), transactionId);
        return transaction != null && transaction.getConfirmations() != null && transaction.getConfirmations() > 0;
    }

    @Override
    // isConfirmed would be set true after the very first confirmation
    public AccountStatus getTransactionStatus(String address) {
        BlockCypherAddress blockCypherAddress = blockCypherClient.getAddress(getCoin(), address);
        boolean isConfirmed = blockCypherAddress.getUnconfirmedBalance().compareTo(BigDecimal.ZERO) == 0;

        String transactionHash = null;
        if (!blockCypherAddress.getTxs().isEmpty())
            transactionHash = blockCypherAddress.getTxs().get(0).getHash();
        return new AccountStatus(isConfirmed, processValue(blockCypherAddress.getUnconfirmedBalance()), processValue(blockCypherAddress.getBalance()), transactionHash);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BlockCypherTransaction transaction = blockCypherClient.getTransaction(getCoin(), txHash);
        validatePaymentDetails(transaction);
        BigDecimal fee = transaction.getFees();

        BigDecimal received = BigDecimal.ZERO;
        for (BlockCypherTransactionOutput output : transaction.getOutputs()) {
            if (output.getAddresses().contains(address))
                received = received.add(output.getValue());
        }

        String buyerAddress = null;
        if (!transaction.getInputs().isEmpty() && !transaction.getInputs().get(0).getAddresses().isEmpty())
            buyerAddress = transaction.getInputs().get(0).getAddresses().get(0);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(this.processValue(received));
        paymentDetails.setSpentValue(this.processValue(received.add(fee)));
        paymentDetails.setFee(this.processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    public BigDecimal processValue(BigDecimal value) {
        return getCoin().convertValue(value);
    }
}
