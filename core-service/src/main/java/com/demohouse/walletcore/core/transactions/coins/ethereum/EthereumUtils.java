package com.demohouse.walletcore.core.transactions.coins.ethereum;

import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECDSASignatureDetails;
import com.demohouse.walletcore.utils.signature.ECSignUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class EthereumUtils {


    public static BigDecimal MIN_GAS_LIMIT = new BigDecimal("21000");
    public static BigDecimal MIN_TETHER_GAS_LIMIT = new BigDecimal("100000");


    public static BigDecimal convertEtherToWei(BigDecimal etherValue) {
        return Convert.toWei(etherValue, Convert.Unit.ETHER);
    }

    public static BigDecimal convertWeiToEther(BigDecimal weiValue) {
        return Convert.fromWei(weiValue, Convert.Unit.ETHER);
    }

    public static ECDSASignatureDetails signRawTx(byte[] message, String privateKey) {
        return ECSignUtils.signMessage(message, privateKey, true);
    }

    public static boolean isAddressValid(String address) {
        if(!address.startsWith("0x") && address.length() != 42) return false;
        try {
            WalletHexUtils.decodeHexString(address);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
