package com.demohouse.walletcore.core.providers.tokenview.helpers;

import com.demohouse.walletcore.core.providers.tokenview.api.TokenViewClient;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.services.sign.SerializeTransactionService;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TokenViewZcashHelper extends TokenViewGenericHelper {

    private static final Coin COIN = Coin.ZCASH;

    public TokenViewZcashHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> genericUTXOS = this.getGeneratedUTXOs(from);
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
