package com.demohouse.walletcore.core.transactions.coins.generic;

import com.demohouse.walletcore.core.addresses.legacy.LegacyAddressGenerator;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECSignUtils;

import java.math.BigDecimal;

public class GenericUtils {

    public static final String OP_SIGNATURE_HASH_CODE_TYPE = "01";
    public static final String OP_DUP = "76";
    public static final String OP_HASH160 = "a9";
    public static final String OP_EQUALVERIFY = "88";
    public static final String OP_CHECKSIG = "ac";
    public static final BigDecimal conversionFactor = new BigDecimal("100000000.0");

    public static String generatePublicKeyScript(String address) {
        final byte[] hash160Bytes = LegacyAddressGenerator.extractHash160FromAddress(address);
        final String hash160 = WalletHexUtils.bytesToHex(hash160Bytes);
        return OP_DUP + OP_HASH160 + WalletHexUtils.longToHex(hash160.length() / 2) + hash160 + OP_EQUALVERIFY + OP_CHECKSIG;
    }

    public static String generateScriptSig(String signature, String privateKey, long hashCodeType) {
        final String hashTypeAppended = signature + WalletHexUtils.longToHex(hashCodeType);
        final String signatureLength = WalletHexUtils.longToHex((hashTypeAppended.length() / 2));
        final String publicKey = ECSignUtils.computePublicKey(privateKey);
        final String publicKeyLength = WalletHexUtils.longToHex(publicKey.length() / 2);
        return signatureLength + hashTypeAppended + publicKeyLength + publicKey;
    }

}
