package com.demohouse.walletcore.core.providers.blockchair.helpers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashTransactionBuilder;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericTransaction;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BlockChairBitcoinCashHelper extends BlockChairGenericHelper {

    private static final Coin COIN = Coin.BITCOINCASH;

    public BlockChairBitcoinCashHelper(BlockChairClient blockChairClient) {
        super(blockChairClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }

    @Override
    public String generatePublicKeyScript(String address) {
        return BitcoinCashUtils.generatePublicKeyScript(address);
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        GenericTransaction genericTransaction = new BitcoinCashTransactionBuilder().build(getCoin(), genericUTXOS, from, payments, fee);
        return genericTransaction.serialize(privateKey);
    }
}
