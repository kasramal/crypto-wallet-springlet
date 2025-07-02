package com.demohouse.walletcore.core.providers.btcexplorer.helpers;

import com.demohouse.walletcore.core.providers.btcexplorer.api.BtcExplorerClient;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class BtcExplorerBitcoinHelper extends BtcExplorerGenericHelper {

    private static final Coin COIN = Coin.BITCOIN;

    public BtcExplorerBitcoinHelper(BtcExplorerClient btcExplorerClient) {
        super(btcExplorerClient);
    }

    @Override
    public Coin getCoin() {
        return COIN;
    }
}
