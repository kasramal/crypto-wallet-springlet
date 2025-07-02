package com.demohouse.walletcore.core.transactions.services;

public enum CryptoCurrencyError {
    API_ERROR("API_ERROR"),
    CORRUPTED_PAYMENT("CORRUPTED_PAYMENT"),
    EMPTY_ADMIN_WALLET("EMPTY_ADMIN_WALLET"),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE"),
    INVALID_CONTRACT_ID("INVALID_CONTRACT_ID"),
    INVALID_ADDRESS("INVALID_ADDRESS"),
    INVALID_TRANSACTION("INVALID_TRANSACTION"),
    MIN_VALUE_VIOLATED("MIN_VALUE_VIOLATED"),
    PROVIDER_TOO_MANY_REQUEST("PROVIDER_TOO_MANY_REQUEST"),
    COIN_NOT_SUPPORTED("COIN_NOT_SUPPORTED"),
    API_DOES_NOT_SUPPORT_PUSH_TX("API_DOES_NOT_SUPPORT_PUSH_TX"),
    TRANSACTION_VALUE_LESS_THAN_FEE("TRANSACTION_VALUE_LESS_THAN_FEE"),
    INSUFFICIENT_GAS_FOR_ERC20_TRANSACTION("INSUFFICIENT_GAS_FOR_ERC20_TRANSACTION"),
    GAS_TANK_NOT_FOUND("GAS_TANK_NOT_FOUND"),
    TANK_IS_OUT_OF_GAS("TANK_IS_OUT_OF_GAS"),
    TRANSACTION_NOT_FOUND("TRANSACTION_NOT_FOUND"),
    TRANSACTION_OWNER_ADDRESS_NOT_VALID("TRANSACTION_OWNER_ADDRESS_NOT_VALID"),
    KEY_PAIR_CREATION_ERROR("KEY_PAIR_CREATION_ERROR"),
    ADDRESS_GENERATE_ERROR("ADDRESS_GENERATE_ERROR"),
    TRANSACTION_NOT_CONFIRMED_YET("TRANSACTION_NOT_CONFIRMED_YET"),
    WALLET_PRIVATE_KEY_NOT_FOUND("WALLET_PRIVATE_KEY_NOT_FOUND");

    private final String name;

    CryptoCurrencyError(String name) {
        this.name = name;
    }

    public static CryptoCurrencyError findByName(String name) {
        for (CryptoCurrencyError c : values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getMessageKey() {
        return "cryptocurrency.error." + name;
    }

}
