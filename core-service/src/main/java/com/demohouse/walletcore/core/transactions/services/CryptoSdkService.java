package com.demohouse.walletcore.core.transactions.services;

import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.core.thirdparty.crypto.dict;
import com.demohouse.walletcore.core.thirdparty.crypto.service;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.utils.WalletHexUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

import static com.demohouse.walletcore.core.thirdparty.crypto.service.transfer_funds;
import static com.demohouse.walletcore.core.thirdparty.crypto.transaction.transaction_decode;
import static com.demohouse.walletcore.core.thirdparty.crypto.wallet.*;

public class CryptoSdkService {

    public static GeneratedAddress generateAddress(Coin coin) {
        String coinName = getCoinName(coin);

        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(256, new SecureRandom());
        } while (randomNumber.toString(16).length() > 256);

        String wif = privatekey_encode(randomNumber, false, coinName, false);
        String pbKey = publickey_from_privatekey(wif, coinName, false);

        String address = address_from_publickey(pbKey, coinName, false);
        return new GeneratedAddress(wif, address);
    }

    public static boolean validateAddress(Coin coin, String address) {
        try {
            address_decode(address, getCoinName(coin), false);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static String serializeTransaction(Coin coin, String privateKey, String from, String to, BigDecimal value, BigDecimal fee, Map<String, Object> values) {
        Mono<String> mono = Mono.create(stringMonoSink -> {
            pushTransactionAsync(
                    coin,
                    privateKey,
                    from,
                    to,
                    value,
                    fee,
                    new CryptoCallback(values) {
                        @Override
                        public String broadcast_txn(byte[] txn, String coin, boolean testnet) {
                            stringMonoSink.success(WalletHexUtils.bytesToHex(txn));
                            return "";
                        }
                    });
        });
        return mono.block();
    }

    private static String getCoinName(Coin coin) {
        return coin.getName();
    }

    public static void pushTransactionAsync(Coin coin, String privateKey, String from, String to, BigDecimal value, BigDecimal fee, service.callback callBack) {
        String coinName = getCoinName(coin);
        transfer_funds(
                privateKey,
                from,
                to,
                value.toBigInteger(),
                fee.toBigInteger(),
                from,
                coinName,
                false,
                callBack);
    }

    public static String retrieveSignatureFromSignedTransaction(Coin coin, String tx) {
        String coinName = getCoinName(coin);
        dict fields = transaction_decode(WalletHexUtils.decodeHexString(tx), coinName, false);
        return WalletHexUtils.bytesToHex(fields.get("signature"));
   }

    private abstract static class CryptoCallback implements service.callback {
        Map<String, Object> values;

        CryptoCallback(Map<String, Object> values) {
            this.values = values;
        }

        @Override
        public BigInteger get_fee(String coin, boolean testnet) {
            return (BigInteger) values.get("fee");
        }

        @Override
        public BigInteger get_balance(String address, String coin, boolean testnet) {
            return (BigInteger) values.get("balance");
        }

        @Override
        public dict[] get_utxos(String address, String coin, boolean testnet) {
            return (dict[]) values.get("utxos");
        }

        @Override
        public BigInteger get_txn_count(String address, String coin, boolean testnet) {
            return (BigInteger) values.get("txn-count");
        }

        @Override
        public Object custom_call(String name, Object arg, String coin, boolean testnet) {
            return values.get(name);
        }
    }

}
