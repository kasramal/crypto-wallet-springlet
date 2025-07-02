package com.demohouse.walletcore.core.providers.sochain.helpers;

import com.demohouse.walletcore.core.providers.sochain.api.SoChainClient;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SoChainZcashHelper extends SoChainGenericHelper {

    public SoChainZcashHelper(SoChainClient soChainClient) {
        super(soChainClient);
    }

    @Override
    public Coin getCoin() {
        return Coin.ZCASH;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        CryptoCurrencyPayment cryptoCurrencyPayment = payments.get(0);
        List<GenericUTXO> genericUTXOS = this.genericUTXOS(from);
        return SerializeTransactionService.serializeZCashTransaction(privateKey, from, cryptoCurrencyPayment.getTo(), cryptoCurrencyPayment.getValue(), fee, genericUTXOS);
    }
}
