package com.demohouse.walletcore.core.addresses.legacy;

import com.demohouse.walletcore.utils.Base58Utils;
import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.RippleBase58Utils;

import java.util.Arrays;

public class WifGenerator {

    public static String generateWif(byte[] privateKey, LegacyAddressVariant variant, boolean userRippleBase58) {

        byte[] toBeHashed = new byte[privateKey.length + 1];
        toBeHashed[0] = variant.getWifPrefix();
        System.arraycopy(privateKey, 0, toBeHashed, 1, privateKey.length);
        String wifHashed = WalletHashUtils.doubleSha256(WalletHexUtils.bytesToHex(toBeHashed));

        byte[] checksum = WalletHexUtils.decodeHexString(wifHashed);
        byte[] wif = new byte[privateKey.length + 5];
        wif[0] = variant.getWifPrefix();
        System.arraycopy(privateKey, 0, wif, 1, privateKey.length);
        System.arraycopy(checksum, 0, wif, privateKey.length + 1, 4);
        return userRippleBase58 ? RippleBase58Utils.encode(wif) : Base58Utils.encode(wif);
    }

    public static String generateWif(byte[] privateKey, LegacyAddressVariant variant) {
        return generateWif(privateKey, variant, false);
    }

    public static String convertWifToPrivateKey(String wif, boolean userRippleBase58) {
        byte[] decoded = userRippleBase58 ? RippleBase58Utils.decode(wif) : Base58Utils.decode(wif);
        byte[] keyBytes = Arrays.copyOfRange(decoded, 1, decoded.length - 4);
        return WalletHexUtils.bytesToHex(keyBytes);
    }

    public static String convertWifToPrivateKey(String wif) {
        return convertWifToPrivateKey(wif, false);
    }
}
