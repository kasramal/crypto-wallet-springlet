package com.demohouse.walletcore.core.addresses.legacy;

import java.security.NoSuchAlgorithmException;

public class DashAddressGenerator extends LegacyAddressGenerator {

    private final LegacyAddressVariant variant = LegacyAddressVariant.DASH;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new DashAddressGenerator().generate();
    }

    @Override
    public LegacyAddressVariant getVariant() {
        return variant;
    }
}
