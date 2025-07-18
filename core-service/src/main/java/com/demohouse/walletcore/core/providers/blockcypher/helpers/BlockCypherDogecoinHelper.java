package com.demohouse.walletcore.core.providers.blockcypher.helpers;

import com.demohouse.walletcore.core.providers.blockcypher.api.BlockCypherClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class BlockCypherDogecoinHelper extends BlockCypherGenericHelper {

    private static final Coin COIN = Coin.DOGECOIN;

    public BlockCypherDogecoinHelper(BlockCypherClient blockCypherClient) {
        super(blockCypherClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
