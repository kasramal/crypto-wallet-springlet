package com.demohouse.walletcore.core.addresses.legacy;

public enum LegacyAddressVariant {
    BITCOIN((byte) 0x00,(byte) 0x00, (byte) 0x80),
    BITCOINCASH((byte) 0x00,(byte) 0x00, (byte) 0x80),
    LITECOIN((byte) 0x30,(byte) 0x00, (byte) 0xb0),
    DASH((byte) 0x4c,(byte) 0x00, (byte) 0xcc),
    RIPPLE((byte) 0x00,(byte) 0x00, (byte) 0x22),
    DOGECOIN((byte) 0x1e,(byte) 0x00, (byte) 0x9e),
    ZCASH((byte) 0x1c,(byte) 0xb8, (byte) 0x80),
    EOS((byte) 0x00,(byte) 0x00, (byte) 0x80);

    private final byte version;
    private final byte secondVersionByte;
    private final byte wifPrefix;

    LegacyAddressVariant(byte version,byte secondVersionByte, byte wifPrefix) {
        this.version = version;
        this.secondVersionByte = secondVersionByte;
        this.wifPrefix = wifPrefix;
    }

    public byte getVersion() {
        return version;
    }

    public byte getSecondVersionByte() {
        return secondVersionByte;
    }

    public boolean hasTwoVersionBytes() {
        return secondVersionByte != 0x00;
    }

    public byte getWifPrefix() {
        return wifPrefix;
    }
}
