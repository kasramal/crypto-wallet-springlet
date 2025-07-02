package com.demohouse.walletcore.core.providers.btcexplorer.helpers;

import com.demohouse.walletcore.core.providers.ApiHelper;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;

public interface BtcExplorerHelper extends ApiHelper {

    @Override
    default CryptoCurrencyApiProvider getProvider() {
        return CryptoCurrencyApiProvider.BTC_EXPLORER;
    }
}
