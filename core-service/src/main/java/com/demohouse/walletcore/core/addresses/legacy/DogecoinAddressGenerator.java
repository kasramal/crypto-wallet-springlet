package com.demohouse.walletcore.core.addresses.legacy;

import java.security.NoSuchAlgorithmException;

public class DogecoinAddressGenerator extends LegacyAddressGenerator {

    private final LegacyAddressVariant variant = LegacyAddressVariant.DOGECOIN;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new DogecoinAddressGenerator().generate();
    }

    @Override
    public LegacyAddressVariant getVariant() {
        return variant;
    }
}
