package com.demohouse.walletcore.core.providers.blockchair.helpers;

import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BlockChairZcashHelper extends BlockChairGenericHelper {

    private static final Coin COIN = Coin.ZCASH;

    public BlockChairZcashHelper(BlockChairClient blockChairClient) {
        super(blockChairClient);
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = getUTXOs(from);
        CryptoCurrencyPayment payment = payments.get(0);
        return SerializeTransactionService.serializeZCashTransaction(
                privateKey,
                from,
                payment.getTo(),
                payment.getValue(),
                fee,
                genericUTXOS);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
