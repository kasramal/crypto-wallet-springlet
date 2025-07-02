package com.demohouse.walletcore.core.providers.blockcypher.helpers;

import com.demohouse.walletcore.core.providers.blockcypher.api.BlockCypherClient;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherAddress;
import com.demohouse.walletcore.core.providers.blockcypher.api.models.BlockCypherTransaction;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumAddress;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransaction;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BlockCypherEthereumHelper implements BlockCypherHelper {

    private static final Coin COIN = Coin.ETHEREUM;

    private final BlockCypherClient blockCypherClient;

    public BlockCypherEthereumHelper(BlockCypherClient blockCypherClient) {
        this.blockCypherClient = blockCypherClient;
    }

    private String preProcessAddress(String address) {
        return address.toLowerCase().replace("0x", "");
    }

    private String postProcessAddress(String address) {
        return "0x" + address;
    }

    private EthereumAddress getAddress(String address) {
        BlockCypherAddress blockCypherAddress = blockCypherClient.getAddress(Coin.ETHEREUM, preProcessAddress(address));
        EthereumAddress ethereumAddress = new EthereumAddress();
        ethereumAddress.setBalance(processValue(blockCypherAddress.getBalance()));
        ethereumAddress.setNonce(blockCypherAddress.getNonce());
        return ethereumAddress;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment payment = payments.get(0);
        EthereumAddress ethereumAddress = getAddress(from);
        EthereumTransaction transaction = EthereumTransactionBuilder.build(ethereumAddress, payment.getTo(), payment.getValue(), fee);
        return transaction.sign(privateKey, true);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        return getAddress(address).getBalance();
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
    public AccountStatus getTransactionStatus(String address) {
        BlockCypherAddress blockCypherAddress = blockCypherClient.getAddress(getCoin(), address);
        boolean isConfirmed = blockCypherAddress.getUnconfirmedBalance().compareTo(BigDecimal.ZERO) == 0
                && blockCypherAddress.getBalance().compareTo(BigDecimal.ZERO) > 0;

        String transactionHash = null;
        if (!blockCypherAddress.getTxs().isEmpty())
            transactionHash = blockCypherAddress.getTxs().get(0).getHash();
        return new AccountStatus(isConfirmed, processValue(blockCypherAddress.getUnconfirmedBalance()), processValue(blockCypherAddress.getBalance()), transactionHash);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BlockCypherAddress blockCypherAddress = blockCypherClient.getAddress(Coin.ETHEREUM, preProcessAddress(address));
        BlockCypherTransaction transaction = blockCypherAddress.getTxs().get(0);

        validatePaymentDetails(transaction);
        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = transaction.getFees();
        BigDecimal receivedValue = transaction.getTotal();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue.add(fee)));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(postProcessAddress(transaction.getInputs().get(0).getAddresses().get(0)));
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return EthereumUtils.convertWeiToEther(value);
    }
}
