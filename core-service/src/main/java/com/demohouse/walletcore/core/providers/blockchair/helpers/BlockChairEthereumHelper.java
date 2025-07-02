package com.demohouse.walletcore.core.providers.blockchair.helpers;

import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairConstants;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairAddressContent;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTransaction;
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
public class BlockChairEthereumHelper implements BlockChairHelper {

    private static final Coin COIN = Coin.ETHEREUM;

    private final BlockChairClient blockChairClient;

    public BlockChairEthereumHelper(BlockChairClient blockChairClient) {
        this.blockChairClient = blockChairClient;
    }

    private EthereumAddress getAddress(String address) {
        BlockChairAddressContent blockChairAddress = blockChairClient.getAddress(getCoin(), address.toLowerCase());
        EthereumAddress ethereumAddress = new EthereumAddress();
        ethereumAddress.setBalance(processValue(blockChairAddress.getAddress().getBalance()));
        ethereumAddress.setNonce(blockChairAddress.getAddress().getNonce());
        return ethereumAddress;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment payment = payments.get(0);
        EthereumAddress ethereumAddress = getAddress(from);
        EthereumTransaction transaction = EthereumTransactionBuilder.build(ethereumAddress, payment.getTo(), payment.getValue(), fee);
        return "0x" + transaction.sign(privateKey, true);
    }

    @Override
    public Coin getCoin() {
        return COIN;
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
    public AccountStatus getTransactionStatus(String address) {
        BlockChairAddressContent blockChairAddressContent = blockChairClient.getAddress(getCoin(), address.toLowerCase());
        BlockChairAddressContent blockChairConfirmedContent = blockChairClient.getAddressConfirmedBalance(getCoin(), address.toLowerCase(), false);

        BigDecimal balance = blockChairConfirmedContent.getAddress().getBalance();
        BigDecimal unconfirmedBalance = blockChairAddressContent.getAddress().getBalance().subtract(balance);

        boolean isConfirmed = unconfirmedBalance.compareTo(BigDecimal.ZERO) == 0;
        String txId = null;
        if (!blockChairAddressContent.getCalls().isEmpty())
            txId = blockChairAddressContent.getCalls().get(0).getTransactionHash();
        return new AccountStatus(
                isConfirmed,
                processValue(unconfirmedBalance),
                processValue(balance),
                txId);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BlockChairTransaction transaction = blockChairClient.getTransaction(getCoin(), txHash).getTransaction();
        validatePaymentDetails(transaction);
        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = transaction.getFee();
        BigDecimal receivedValue = transaction.getValue();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue.add(fee)));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setTxId(txHash);
        paymentDetails.setBuyerAddress(transaction.getSender());
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        if (value == null) return BigDecimal.ZERO;
        return EthereumUtils.convertWeiToEther(value);
    }
}
