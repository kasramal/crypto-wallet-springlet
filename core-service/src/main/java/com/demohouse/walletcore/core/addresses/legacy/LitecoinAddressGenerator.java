package com.demohouse.walletcore.core.addresses.legacy;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class LitecoinAddressGenerator extends LegacyAddressGenerator {

    private final LegacyAddressVariant variant = LegacyAddressVariant.LITECOIN;

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        new LitecoinAddressGenerator().generate();
    }

    @Override
    public LegacyAddressVariant getVariant() {
        return variant;
    }
}
