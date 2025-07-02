package com.demohouse.walletcore.core.providers.btcexplorer.helpers;

import com.demohouse.walletcore.core.providers.btcexplorer.api.BtcExplorerClient;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerAddress;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransaction;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransactionList;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransactionOutput;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;

import java.math.BigDecimal;
import java.util.List;

public abstract class BtcExplorerGenericHelper implements BtcExplorerHelper {


    private final BtcExplorerClient btcExplorerClient;

    public BtcExplorerGenericHelper(BtcExplorerClient btcExplorerClient) {
        this.btcExplorerClient = btcExplorerClient;
    }

    public List<GenericUTXO> getUTXOs(String address) {
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_DOES_NOT_SUPPORT_PUSH_TX);
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new GenericTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        return this.processValue(btcExplorerClient.getAddress(getCoin(), processAddress(address)).getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        BtcExplorerAddress btcExplorerAddress = btcExplorerClient.getAddress(getCoin(), processAddress(address));
        return btcExplorerAddress.getUnconfirmedTxCount() == null || btcExplorerAddress.getUnconfirmedTxCount().compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        BtcExplorerTransaction transaction = btcExplorerClient.getTransaction(getCoin(), transactionId);
        return transaction != null && transaction.getBlockHeight() != null && transaction.getBlockHeight().compareTo(BigDecimal.ONE) > 0;
    }

    @Override
    // isConfirmed would be set true after the first confirmation
    public AccountStatus getTransactionStatus(String address) {
        BtcExplorerAddress btcExplorerAddress = btcExplorerClient.getAddress(getCoin(), processAddress(address));
        boolean isConfirmed =
                (btcExplorerAddress.getUnconfirmedTxCount() == null || btcExplorerAddress.getUnconfirmedTxCount().compareTo(BigDecimal.ZERO) == 0);

        BigDecimal unconfirmedBalance = btcExplorerAddress.getUnconfirmedTxCount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : btcExplorerAddress.getUnconfirmedReceived().subtract(btcExplorerAddress.getUnconfirmedSent());
        BtcExplorerTransactionList btcExplorerTransactionList = btcExplorerClient.getTransactionsOfAddress(getCoin(), processAddress(address));
        String transactionHash = null;
        if (btcExplorerTransactionList != null && !btcExplorerTransactionList.getList().isEmpty())
            transactionHash = btcExplorerTransactionList.getList().get(0).getHash();
        return new AccountStatus(isConfirmed, processValue(unconfirmedBalance), processValue(btcExplorerAddress.getBalance()), transactionHash);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BtcExplorerTransaction transaction = btcExplorerClient.getTransaction(getCoin(), txHash);
        validatePaymentDetails(transaction);

        BigDecimal fee = transaction.getFee();

        BigDecimal received = BigDecimal.ZERO;
        for (BtcExplorerTransactionOutput output : transaction.getOutputs()) {
            if (output.getAddresses().contains(processAddress(address)))
                received = received.add(output.getValue());
        }

        String buyerAddress = null;
        if (!transaction.getInputs().isEmpty() && !transaction.getInputs().get(0).getPrevAddresses().isEmpty())
            buyerAddress = transaction.getInputs().get(0).getPrevAddresses().get(0);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(BitcoinUtils.satoshiToBitcoin(received));
        paymentDetails.setSpentValue(BitcoinUtils.satoshiToBitcoin(received.add(fee)));
        paymentDetails.setFee(BitcoinUtils.satoshiToBitcoin(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    public BigDecimal processValue(BigDecimal value) {
        return getCoin().convertValue(value);
    }

    public String processAddress(String address) {
        return address;
    }
}
