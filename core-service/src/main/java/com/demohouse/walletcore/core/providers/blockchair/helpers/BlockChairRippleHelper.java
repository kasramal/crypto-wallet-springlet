package com.demohouse.walletcore.core.providers.blockchair.helpers;

import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairConstants;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairAddressContent;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTransaction;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTransactionCall;
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
public class BlockChairRippleHelper implements BlockChairHelper {

    private static final Coin COIN = Coin.ETHEREUM;

    private final BlockChairClient blockChairClient;

    public BlockChairRippleHelper(BlockChairClient blockChairClient) {
        this.blockChairClient = blockChairClient;
    }

    private EthereumAddress getAddress(String address) {
        BlockChairAddressContent blockChairAddress = blockChairClient.getAddress(Coin.RIPPLE, address.toLowerCase());
        EthereumAddress ethereumAddress = new EthereumAddress();
        ethereumAddress.setBalance(EthereumUtils.convertWeiToEther(blockChairAddress.getAddress().getBalance()));
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
        return processValue(getAddress(address).getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        BlockChairAddressContent blockChairAddress = blockChairClient.getAddress(Coin.ETHEREUM, address.toLowerCase());
        BlockChairTransactionCall call = blockChairAddress.getCalls().get(0);
        return call.getBlockId().longValue() != BlockChairConstants.UNCONFIRMED_BLOCK_ID;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        BlockChairTransaction transaction = blockChairClient.getTransaction(getCoin(), transactionId).getTransaction();
        return transaction.getBlockId() != BlockChairConstants.UNCONFIRMED_BLOCK_ID;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        BlockChairAddressContent blockChairAddress = blockChairClient.getAddress(Coin.ETHEREUM, address.toLowerCase());

        if (blockChairAddress.getCalls().size() == 0) return AccountStatus.UNCONFIRMED;

        BlockChairTransactionCall call = blockChairAddress.getCalls().get(0);

        boolean isConfirmed = call.getBlockId().longValue() != BlockChairConstants.UNCONFIRMED_BLOCK_ID;
        BigDecimal balance = processValue(blockChairAddress.getAddress().getBalance());
        return new AccountStatus(
                isConfirmed,
                balance,
                call.getTransactionHash());
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        BlockChairTransaction transaction = blockChairClient.getTransaction(Coin.ETHEREUM, txHash).getTransaction();
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
        return EthereumUtils.convertWeiToEther(value);
    }
}
