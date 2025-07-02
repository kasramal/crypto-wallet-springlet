package com.demohouse.walletcore.core.providers.tronprovider.helpers;

import com.demohouse.walletcore.core.providers.ApiHelper;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;

public interface TronHelper extends ApiHelper {

    @Override
    default CryptoCurrencyApiProvider getProvider() {
        return CryptoCurrencyApiProvider.TRON_PROVIDER;
    }
}
