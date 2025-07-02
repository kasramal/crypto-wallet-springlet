package com.demohouse.walletcore.core.providers.zcha.helpers;

import com.demohouse.walletcore.core.providers.ApiHelper;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;

public interface ZchaHelper extends ApiHelper {

    @Override
    default CryptoCurrencyApiProvider getProvider() {
        return CryptoCurrencyApiProvider.ZCHA;
    }
}
