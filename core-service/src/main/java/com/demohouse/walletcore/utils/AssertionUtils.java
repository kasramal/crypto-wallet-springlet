package com.demohouse.walletcore.utils;

public class AssertionUtils {

    public static void assertThat(boolean condition, String errorMessage) {
        if(condition) {
            throw new RuntimeException(errorMessage);
        }
    }
}
