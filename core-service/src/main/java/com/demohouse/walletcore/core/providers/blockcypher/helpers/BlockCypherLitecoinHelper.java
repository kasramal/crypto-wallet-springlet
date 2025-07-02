package com.demohouse.walletcore.core.providers.blockcypher.helpers;

import com.demohouse.walletcore.core.providers.blockcypher.api.BlockCypherClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class BlockCypherLitecoinHelper extends BlockCypherGenericHelper {

    private static final Coin COIN = Coin.LITECOIN;

    public BlockCypherLitecoinHelper(BlockCypherClient blockCypherClient) {
        super(blockCypherClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
