package com.demohouse.walletcore.core.providers;

public enum BalanceCalculationMode {
    UTXO_BASED_BALANCE_CALCULATION(1),
    TRANSACTION_BASED_BALANCE_CALCULATION(2),
    CONFIRMED_BALANCE_CALCULATION(3);

    private int code;

    BalanceCalculationMode(int code) {
        this.code = code;
    }

    public static BalanceCalculationMode findByCode(int code) {
        for (BalanceCalculationMode c : values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

}
