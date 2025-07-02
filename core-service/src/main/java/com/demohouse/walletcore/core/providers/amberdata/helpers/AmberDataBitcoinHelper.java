package com.demohouse.walletcore.core.providers.amberdata.helpers;

import com.demohouse.walletcore.core.providers.amberdata.api.AmberDataClient;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AmberDataBitcoinHelper extends AmberDataGenericHelper {

    private static final Coin COIN = Coin.BITCOIN;

    public AmberDataBitcoinHelper(AmberDataClient amberDataClient) {
        super(amberDataClient);
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> utxOs = this.getUTXOs(from);
        CryptoCurrencyPayment cryptoCurrencyPayment = payments.get(0);
        return SerializeTransactionService.serializeBitcoinTransaction(privateKey, from, cryptoCurrencyPayment.getTo(), cryptoCurrencyPayment.getValue(), fee, utxOs);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
