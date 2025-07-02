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
public class TokenViewBitcoinHelper extends TokenViewGenericHelper {

    public TokenViewBitcoinHelper(TokenViewClient tokenViewClient) {
        super(tokenViewClient);
    }

    @Override
    public Coin getCoin() {
        return Coin.BITCOIN;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        List<GenericUTXO> generatedUTXOs = super.getGeneratedUTXOs(from);
        CryptoCurrencyPayment cryptoCurrencyPayment = payments.get(0);
        return SerializeTransactionService.serializeBitcoinTransaction(privateKey, from, cryptoCurrencyPayment.getTo(), cryptoCurrencyPayment.getValue(), fee, generatedUTXOs);
    }
}
