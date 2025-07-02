package com.demohouse.walletcore.core.providers.blockchair.helpers;

import com.demohouse.walletcore.core.providers.blockchair.api.BlockChairClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class BlockChairLitecoinHelper extends BlockChairGenericHelper {

    private static final Coin COIN = Coin.LITECOIN;

    public BlockChairLitecoinHelper(BlockChairClient blockChairClient) {
        super(blockChairClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
