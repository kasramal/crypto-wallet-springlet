package com.demohouse.walletcore.core.providers.cryptonomic.helpers;

import com.demohouse.walletcore.core.providers.ApiHelper;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;

public interface CryptoNomicHelper extends ApiHelper {

    @Override
    default CryptoCurrencyApiProvider getProvider() {
        return CryptoCurrencyApiProvider.CRYPTO_NOMIC;
    }
}
