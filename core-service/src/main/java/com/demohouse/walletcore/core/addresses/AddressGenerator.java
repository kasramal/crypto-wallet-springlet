package com.demohouse.walletcore.core.addresses;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public interface AddressGenerator {
    GeneratedAddress generate() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
}
