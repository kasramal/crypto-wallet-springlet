package com.demohouse.walletcore.core.providers.btcexplorer.helpers;

import com.demohouse.walletcore.core.providers.btcexplorer.api.BtcExplorerClient;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransaction;
import com.demohouse.walletcore.core.providers.btcexplorer.api.models.BtcExplorerTransactionOutput;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BtcExplorerBitcoinCashHelper extends BtcExplorerGenericHelper {

    private static final Coin COIN = Coin.BITCOINCASH;
    private final BtcExplorerClient btcExplorerClient;

    public BtcExplorerBitcoinCashHelper(BtcExplorerClient btcExplorerClient) {
        super(btcExplorerClient);
        this.btcExplorerClient = btcExplorerClient;
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new BitcoinCashTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BtcExplorerTransaction transaction = btcExplorerClient.getTransaction(getCoin(), txHash);
        validatePaymentDetails(transaction);

        BigDecimal fee = transaction.getFee();

        BigDecimal received = BigDecimal.ZERO;
        String p2pkhAddress = BitcoinCashUtils.getP2PkhAddress(address);
        for (BtcExplorerTransactionOutput output : transaction.getOutputs()) {
            if (output.getAddresses().contains(p2pkhAddress))
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
        paymentDetails.setBuyerAddress(BitcoinCashUtils.getBchAddressFromP2pkhAddress(buyerAddress));
        return paymentDetails;
    }

    @Override
    public String processAddress(String address) {
        return BitcoinCashUtils.getP2PkhAddress(address);
    }
}
