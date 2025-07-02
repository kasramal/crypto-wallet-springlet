package com.demohouse.walletcore.core.fees;

import com.demohouse.walletcore.core.fees.api.WalletFeeModule;
import com.demohouse.walletcore.core.fees.api.coins.*;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Component;

@Component
public class FeeUtils {


    private final WalletFeeModule feeModule;

    public FeeUtils(WalletFeeModule feeModule) {
        this.feeModule = feeModule;
    }


    public EstimatedFee getFee(Coin coin) {
        switch (coin) {
            case BITCOIN:
                return new BitcoinFee(feeModule.getBitcoinFee());
            case LITECOIN:
                return new LitecoinFee();
            case DOGECOIN:
                return new DogecoinFee();
            case DASH:
                return new DashFee();
            case ZCASH:
                return new ZcashFee();
            case RIPPLE:
                return new RippleFee();
            case BITCOINCASH:
                return new BitcoinCashFee();
            case BITCOINSV:
                return new BitcoinSVFee();
            case ETHEREUM:
                return new EthereumFee(feeModule.getGasPrice(), EthereumUtils.MIN_GAS_LIMIT);
            case ETHEREUM_CLASSIC:
                return new EthereumClassicFee(EthereumUtils.MIN_GAS_LIMIT);
            case TETHER:
                return new EthereumFee(feeModule.getGasPrice(), EthereumUtils.MIN_TETHER_GAS_LIMIT);
            case TRON:
                return new TronFee();
            case TEZOS:
                return new TezosFee();
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

}
