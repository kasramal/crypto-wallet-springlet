package com.demohouse.walletcore.core.addresses;

import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashUtils;
import com.demohouse.walletcore.core.transactions.coins.bitcoinsv.BitcoinSVUtils;
import com.demohouse.walletcore.core.transactions.coins.dash.DashUtils;
import com.demohouse.walletcore.core.transactions.coins.dogecoin.DogecoinUtils;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.coins.litecoin.LitecoinUtils;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.transactions.services.CryptoSdkService;
import com.demohouse.walletcore.entities.Coin;

public class AddressValidatorFactory {

    public static void validateAddress(Coin coin, String address) {
        switch (coin) {
            case BITCOIN:
                if (BitcoinUtils.isAddressInvalid(address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
                break;
            case LITECOIN:
                if (LitecoinUtils.isAddressInvalid(address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
                break;
            case DOGECOIN:
                if (DogecoinUtils.isAddressInvalid(address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
                break;
            case BITCOINCASH:
                if (BitcoinCashUtils.isAddressInvalid(address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
                break;
            case BITCOINSV:
                if (BitcoinSVUtils.isAddressInvalid(address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
                break;
            case DASH:
                if (DashUtils.isAddressInvalid(address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
                break;
            case ETHEREUM:
            case TETHER:
                if (!EthereumUtils.isAddressValid(address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
                break;
            default:
                if (!CryptoSdkService.validateAddress(coin, address))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_ADDRESS);
        }
    }
}
