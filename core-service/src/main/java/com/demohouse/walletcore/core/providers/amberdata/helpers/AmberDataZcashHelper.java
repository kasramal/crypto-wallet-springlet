package com.demohouse.walletcore.core.providers.amberdata.helpers;

import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AmberDataZcashHelper extends AmberDataGenericHelper {

    private static final Coin COIN = Coin.ZCASH;

    public AmberDataZcashHelper(AmberDataClient amberDataClient) {
        super(amberDataClient);
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
