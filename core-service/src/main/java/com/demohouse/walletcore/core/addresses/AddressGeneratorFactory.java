package com.demohouse.walletcore.core.addresses;

import com.demohouse.walletcore.core.addresses.cashaddress.BitcoinCashAddressGenerator;
import com.demohouse.walletcore.core.addresses.ethereum.EthereumAddressGenerator;
import com.demohouse.walletcore.core.addresses.legacy.*;
import com.demohouse.walletcore.core.addresses.tezos.TezosAddressGenerator;
import com.demohouse.walletcore.core.addresses.tron.TronAddressGenerator;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;

public class AddressGeneratorFactory {

    public static AddressGenerator getAddressGenerator(Coin coin) {
        switch (coin) {
            case TETHER:
            case ETHEREUM:
            case ETHEREUM_CLASSIC:
                return new EthereumAddressGenerator();
            case BITCOIN:
            case BITCOINSV:
                return new BitcoinAddressGenerator();
            case DOGECOIN:
                return new DogecoinAddressGenerator();
            case DASH:
                return new DashAddressGenerator();
            case LITECOIN:
                return new LitecoinAddressGenerator();
            case BITCOINCASH:
                return new BitcoinCashAddressGenerator();
            case TRON:
                return new TronAddressGenerator();
            case TEZOS:
                return new TezosAddressGenerator();
            case ZCASH:
                return new ZcashAddressGenerator();
            default:
                throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
        }
    }
}
