package com.demohouse.walletcore.core.providers.blockchair.helpers;

import com.demohouse.walletcore.core.providers.ApiHelper;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;

public interface BlockChairHelper extends ApiHelper {

    @Override
    default CryptoCurrencyApiProvider getProvider() {
        return CryptoCurrencyApiProvider.BLOCK_CHAIR;
    }
}
