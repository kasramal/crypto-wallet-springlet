package com.demohouse.walletcore.core.providers.blockcypher.helpers;

import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairConstants;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairAddressContent;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairErc20Token;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTokenContent;
import com.demohouse.walletcore.core.providers.blockchair.api.models.BlockChairTransaction;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Address;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Token;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20Transaction;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.Erc20TransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.tether.TetherUtils;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//@Component
public class BlockCypherTetherHelper implements BlockCypherHelper {

    private static final Coin COIN = Coin.TETHER;

    private final BlockChairClient blockChairClient;

    public BlockCypherTetherHelper(BlockChairClient blockChairClient) {
        this.blockChairClient = blockChairClient;
    }


    private Erc20Address getAddress(String address) {
        BlockChairAddressContent blockChairAddress = getBlockChairAddressContent(address);
        Erc20Address erc20Address = new Erc20Address();

        BigDecimal nonce = blockChairAddress.getAddress().getNonce();
        erc20Address.setNonce(nonce == null ? new BigDecimal("0") : nonce);

        List<Erc20Token> tokens = new ArrayList<>();
        for (BlockChairErc20Token tokenItt : blockChairAddress.getLayer2().getErc20()) {
            Erc20Token tokenToBeAdded = new Erc20Token();
            tokenToBeAdded.setBalance(tokenItt.getBalance());
            tokenToBeAdded.setTokenAddress(tokenItt.getTokenAddress());
            tokenToBeAdded.setTokenSymbol(tokenItt.getTokenSymbol());
            tokens.add(tokenToBeAdded);
        }
        erc20Address.setTokens(tokens);

        return erc20Address;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment payment = payments.get(0);
        Erc20Address erc20Address = getAddress(from);
        Erc20Transaction transaction = Erc20TransactionBuilder.build(Coin.TETHER, erc20Address, payment.getTo(), payment.getValue(), fee);
        return "0x" + transaction.sign(privateKey, true);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    private BlockChairAddressContent getBlockChairAddressContent(String address) {
        return blockChairClient.getAddress(Coin.ETHEREUM, address.toLowerCase(), true);
    }

    private BlockChairErc20Token getAddressTetherToken(BlockChairAddressContent blockChairAddress) {
        BigDecimal balance = BigDecimal.ZERO;
        for (BlockChairErc20Token token : blockChairAddress.getLayer2().getErc20()) {
            if (token.getTokenSymbol().equals(COIN.getIso()))
                return token;
        }
        return null;
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        BlockChairAddressContent blockChairAddress = getBlockChairAddressContent(address);
        BlockChairErc20Token token = getAddressTetherToken(blockChairAddress);
        return token != null ? processValue(token.getBalance()) : BigDecimal.ZERO;
    }

    @Override
    public Boolean isConfirmed(String address) {
        BlockChairAddressContent blockChairAddress = getBlockChairAddressContent(address);
        BlockChairErc20Token token = getAddressTetherToken(blockChairAddress);
        return token != null;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        BlockChairTransaction transaction = blockChairClient.getTransaction(COIN, transactionId, true).getTransaction();

        return transaction.getBlockId() != BlockChairConstants.UNCONFIRMED_BLOCK_ID;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        BlockChairAddressContent blockChairAddress = getBlockChairAddressContent(address);
        BlockChairErc20Token token = getAddressTetherToken(blockChairAddress);
        if (token == null)
            return AccountStatus.UNCONFIRMED;

        return new AccountStatus(
                true,
                processValue(token.getBalance()),
                token.getTokenAddress());
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String tokenAddress, String address) {

        BlockChairTokenContent tokenContent = blockChairClient.getERC20TokenHolderDetails(tokenAddress, address.toLowerCase());
        BlockChairTransaction tokenTransaction = null;

        for (BlockChairTransaction transaction : tokenContent.getTransactions()) {
            if (transaction.getTokenSymbol().equals(COIN.getIso()))
                tokenTransaction = transaction;
        }

        BlockChairTransaction transaction = blockChairClient.getTransaction(COIN, tokenTransaction.getTransactionHash(), true).getTransaction();

        PaymentDetails paymentDetails = new PaymentDetails();
        BigDecimal fee = transaction.getFee();
        BigDecimal receivedValue = tokenTransaction.getValue();
        paymentDetails.setReceivedValue(processValue(receivedValue));
        paymentDetails.setSpentValue(processValue(receivedValue));
        paymentDetails.setFee(processValue(fee));
        paymentDetails.setBuyerAddress(transaction.getSender());
        paymentDetails.setTxId(tokenTransaction.getTransactionHash());
        paymentDetails.setTokenAddress(tokenAddress);
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return TetherUtils.convertTetherToUSDT(value);
    }
}
