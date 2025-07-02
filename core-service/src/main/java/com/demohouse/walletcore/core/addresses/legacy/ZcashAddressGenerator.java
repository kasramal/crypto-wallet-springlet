package com.demohouse.walletcore.core.addresses.legacy;

import java.security.NoSuchAlgorithmException;

public class ZcashAddressGenerator extends LegacyAddressGenerator {

    private final LegacyAddressVariant variant = LegacyAddressVariant.ZCASH;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new ZcashAddressGenerator().generate();
    }

    @Override
    public LegacyAddressVariant getVariant() {
        return variant;
    }
}
