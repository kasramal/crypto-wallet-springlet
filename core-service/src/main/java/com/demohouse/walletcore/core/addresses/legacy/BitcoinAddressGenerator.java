package com.demohouse.walletcore.core.addresses.legacy;

import java.security.NoSuchAlgorithmException;

public class BitcoinAddressGenerator extends LegacyAddressGenerator {

    private final LegacyAddressVariant variant = LegacyAddressVariant.BITCOIN;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new BitcoinAddressGenerator().generate();
    }

    @Override
    public LegacyAddressVariant getVariant() {
        return variant;
    }
}
