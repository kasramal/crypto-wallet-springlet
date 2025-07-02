package com.demohouse.walletcore.core.transactions.coins.ethereum;

import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class EthereumClassicTransactionValidator extends EthereumTransactionValidator {

    @Override
    public Coin getCoin() {
        return Coin.ETHEREUM_CLASSIC;
    }
}
