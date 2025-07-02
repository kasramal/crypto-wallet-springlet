package com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.tether;

import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransactionValidator;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class TetherTransactionValidator extends EthereumTransactionValidator {

    @Override
    public Coin getCoin() {
        return Coin.TETHER;
    }
}
