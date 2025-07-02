package com.demohouse.walletcore.core.providers.whatsonchain.helper;

import com.demohouse.walletcore.core.providers.ApiHelper;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;

public interface WhatsOnChainHelper extends ApiHelper {

    @Override
    default CryptoCurrencyApiProvider getProvider() {
        return CryptoCurrencyApiProvider.WHATS_ON_CHAIN;
    }
}
