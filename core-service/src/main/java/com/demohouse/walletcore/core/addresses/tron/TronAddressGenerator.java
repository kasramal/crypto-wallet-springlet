package com.demohouse.walletcore.core.addresses.tron;

import com.demohouse.walletcore.core.addresses.AddressGenerator;
import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.core.transactions.services.CryptoSdkService;
import com.demohouse.walletcore.entities.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TronAddressGenerator implements AddressGenerator {

    private final Logger logger = LoggerFactory.getLogger(TronAddressGenerator.class);

    public static void main(String[] args) {
        new TronAddressGenerator().generate();
    }

    @Override
    public GeneratedAddress generate() {
        GeneratedAddress generatedAddress = CryptoSdkService.generateAddress(Coin.TRON);
        logger.debug(String.format("Tron Address Generator -> PrivateKey: %s || Address: %S", generatedAddress.getPrivateKey(), generatedAddress.getAddress()));
        return generatedAddress;
    }
}
