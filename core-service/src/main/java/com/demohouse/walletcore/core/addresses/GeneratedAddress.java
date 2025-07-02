package com.demohouse.walletcore.core.addresses;

public class GeneratedAddress {
    private String privateKey;
    private String address;

    public GeneratedAddress(String privateKey, String address) {
        this.privateKey = privateKey;
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
