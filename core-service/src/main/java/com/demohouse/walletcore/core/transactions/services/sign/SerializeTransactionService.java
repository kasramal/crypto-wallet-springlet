package com.demohouse.walletcore.core.transactions.services.sign;

import com.demohouse.walletcore.core.thirdparty.crypto.dict;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashUtils;
import com.demohouse.walletcore.core.transactions.coins.bitcoinsv.BitcoinSVUtils;
import com.demohouse.walletcore.core.transactions.coins.dash.DashUtils;
import com.demohouse.walletcore.core.transactions.coins.dogecoin.DogecoinUtils;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.coins.generic.GenericUTXO;
import com.demohouse.walletcore.core.transactions.coins.litecoin.LitecoinUtils;
import com.demohouse.walletcore.core.transactions.coins.tron.TronUtils;
import com.demohouse.walletcore.core.transactions.coins.zcash.ZcashUtils;
import com.demohouse.walletcore.core.transactions.services.CryptoSdkService;
import com.demohouse.walletcore.core.transactions.services.sign.entities.TronBlock;
import com.demohouse.walletcore.entities.Coin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SerializeTransactionService {

    /**
     * This method is purposed to serialize a tron transaction using given parameters
     * Sample:
     * TronBlock tronBlock = new TronBlock();
     * tronBlock.setTimestamp(1593330360703);
     * tronBlock.setHash("ffffffffffffffff" //you should add this to start of any hash + "525dd9b81d10fb7e");
     * tronBlock.setHeight(49852);
     * SerializeTransactionService
     * .serializeTronTransaction(
     * "6a819498404964e864adfafc600204f0d30bc0e92f5a8ec05ade58437981749a",
     * "TF4FUkP2jVMp3YUjPHY1UiQBHHqsuwbTBU",
     * "TMd5imKzcqcjvhTdQPZRFp2EaEr3mKtAZH",
     * TronUtils.trxToSun(0.000001),
     * BigDecimal.ZERO,
     * tronBlock
     * )
     *
     * @param privateKey the privateKey of the account
     * @param from       address of the source account
     * @param to         the address of the destination account
     * @param value      the value of the transaction
     * @param fee        the fee of the transaction
     * @param tronBlock  contains (height, hash, timestamp) must be requested from api provider
     * @return
     */

    public static String serializeTronTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, TronBlock tronBlock) {
        Map<String, Object> values = new HashMap<>();

        dict block = new dict();
        block.put("height", tronBlock.getHeight());
        block.put("hash", tronBlock.getHash());
        block.put("timestamp", tronBlock.getTimestamp());

        values.put("block", block);

        return CryptoSdkService.serializeTransaction(Coin.TRON, privateKey, from, to, TronUtils.trxToSun(value), TronUtils.trxToSun(fee), values);
    }

    /**
     * This method is purposed to serialize a transaction for bitcoin-like coins (Bitcoin, Litecoin, Dogecoin, Dash, BitcoinCash, BitcoinSV, ZCash) using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * List<GenericUTXO> utxos = new ArrayList<>();
     * utxos.add(new GenericUTXO("5c0f1f1904488235d77d7b0efca43ed8c7a7d61b46e1d8f47ad21e94d2a81051", 0, "", "Xg3nrBxGRfQQL8nqYHDV722S1JcNGGRhvK", new BigDecimal("0.00100000")));
     * utxos.add(new GenericUTXO("93dcf6ddf30a768bf3864642a4342025d3154600e05b3b556498e2ccb9cbd999", 1, "", "Xg3nrBxGRfQQL8nqYHDV722S1JcNGGRhvK", new BigDecimal("0.01407000")));
     * <p>
     * SerializeTransactionService.serializeBitcoinLikeTransaction(
     * Coin.DASH,
     * "7qhmYq2hRQAWk4vGLNpvKX6bdSzRpofityB25qKm2mmWW8sxcGS",
     * "Xg3nrBxGRfQQL8nqYHDV722S1JcNGGRhvK",
     * "XbtvuiCxf6nz5Bd6ziufniViuLUbXdT3aq",
     * new BigDecimal("0.00100000"),
     * new BigDecimal("0.00002750"),
     * utxos);
     *
     * @param coin         coin
     * @param privateKey   the privateKey of the account
     * @param from         address of the source account
     * @param to           the address of the destination account
     * @param value        the value of the transaction
     * @param fee          the fee of the transaction
     * @param genericUTXOs contains (transactionId, outputIndex, address, amount) must be requested from api provider
     * @return
     */

    public static String serializeBitcoinLikeTransaction(Coin coin, String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        Map<String, Object> values = new HashMap<>();

        dict[] utxos = new dict[genericUTXOs.size()];
        for (int index = 0; index < genericUTXOs.size(); index++) {
            validateGenericUtxoParameters(genericUTXOs.get(index));
            GenericUTXO utxo = genericUTXOs.get(index);
            dict utxoDict = new dict();
            utxoDict.put("txnid", utxo.getTransactionHash());
            utxoDict.put("index", BigInteger.valueOf(utxo.getTransactionIndex()));
            utxoDict.put("address", utxo.getAddress());
            utxoDict.put("amount", utxo.getValue().toBigInteger());
            utxos[index] = utxoDict;
        }

        values.put("utxos", utxos);
        return CryptoSdkService.serializeTransaction(coin, privateKey, from, to, value, fee, values);
    }

    public static String serializeZCashTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(
                Coin.ZCASH,
                privateKey,
                from,
                to,
                ZcashUtils.zcashToSatoshi(value),
                ZcashUtils.zcashToSatoshi(fee),
                processUTXOs(genericUTXOs, (x) -> ZcashUtils.zcashToSatoshi(x)));
    }

    public static String serializeDashTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(
                Coin.DASH,
                privateKey,
                from,
                to,
                DashUtils.dashToSatoshi(value),
                DashUtils.dashToSatoshi(fee),
                processUTXOs(genericUTXOs, (x) -> DashUtils.dashToSatoshi(x)));
    }

    public static String serializeDogecoinTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(
                Coin.DOGECOIN,
                privateKey,
                from,
                to,
                DogecoinUtils.dogecoinToSatoshi(value),
                DogecoinUtils.dogecoinToSatoshi(fee),
                processUTXOs(genericUTXOs, (x) -> DogecoinUtils.dogecoinToSatoshi(x)));
    }

    public static String serializeLitecoinTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(
                Coin.LITECOIN,
                privateKey,
                from,
                to,
                LitecoinUtils.litecoinToSatoshi(value),
                LitecoinUtils.litecoinToSatoshi(fee),
                processUTXOs(genericUTXOs, (x) -> LitecoinUtils.litecoinToSatoshi(x)));
    }

    public static String serializeBitcoinTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(
                Coin.BITCOIN,
                privateKey,
                from,
                to,
                BitcoinUtils.bitcoinToSatoshi(value),
                BitcoinUtils.bitcoinToSatoshi(fee),
                processUTXOs(genericUTXOs, (x) -> BitcoinUtils.bitcoinToSatoshi(x)));
    }

    public static String serializeBitcoinCashTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(
                Coin.BITCOINCASH,
                privateKey,
                from,
                to,
                BitcoinCashUtils.bitcoinCashToSatoshi(value),
                BitcoinCashUtils.bitcoinCashToSatoshi(fee),
                processUTXOs(genericUTXOs, (x) -> BitcoinCashUtils.bitcoinCashToSatoshi(x)));
    }

    public static String serializeBitcoinSVTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(
                Coin.BITCOINSV,
                privateKey,
                from,
                to,
                BitcoinSVUtils.bitcoinSVToSatoshi(value),
                BitcoinSVUtils.bitcoinSVToSatoshi(fee),
                processUTXOs(genericUTXOs, (x) -> BitcoinSVUtils.bitcoinSVToSatoshi(x)));
    }

    /**
     * This method is purposed to serialize a transaction for cardano coins using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * List<GenericUTXO> utxos = new ArrayList<>();
     * utxos.add(new GenericUTXO("5c0f1f1904488235d77d7b0efca43ed8c7a7d61b46e1d8f47ad21e94d2a81051", 0, "", "Ae2tdPwUPEYzHq1S5bZ3ZnLLUzxprj3VMn9u8yD5qxSg4YmhByXhcunMKk2", new BigDecimal("100000")));
     * utxos.add(new GenericUTXO("93dcf6ddf30a768bf3864642a4342025d3154600e05b3b556498e2ccb9cbd999", 1, "", "Ae2tdPwUPEYzHq1S5bZ3ZnLLUzxprj3VMn9u8yD5qxSg4YmhByXhcunMKk2", new BigDecimal("1407000")));
     * <p>
     * SerializeTransactionService.serializeCardanoTransaction(
     * "e864002835604c13fe0ee3b0a8276144569ec2b5c0db29bb3e803c37ebb63d0e",
     * "Ae2tdPwUPEYzHq1S5bZ3ZnLLUzxprj3VMn9u8yD5qxSg4YmhByXhcunMKk2",
     * "Ae2tdPwUPEZAy6aqXZmUKV3uHRXdpSGa5Eb3rm2UyjgYUGE4mXP73iuyEK5",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"),
     * utxos);
     *
     * @param privateKey   the privateKey of the account
     * @param from         address of the source account
     * @param to           the address of the destination account
     * @param value        the value of the transaction
     * @param fee          the fee of the transaction
     * @param genericUTXOs contains (transactionId, outputIndex, address, amount) must be requested from api provider
     * @return
     */

    public static String serializeCardanoTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(Coin.CARDANO, privateKey, from, to, value, fee, genericUTXOs);
    }

    /**
     * This method is purposed to serialize a transaction for neo coins using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * List<GenericUTXO> utxos = new ArrayList<>();
     * utxos.add(new GenericUTXO("5c0f1f1904488235d77d7b0efca43ed8c7a7d61b46e1d8f47ad21e94d2a81051", 0, "", "AaZa2YKVU1LrNpqnyEpsWk8xNJJvkJ4wHe", new BigDecimal("100000")));
     * utxos.add(new GenericUTXO("93dcf6ddf30a768bf3864642a4342025d3154600e05b3b556498e2ccb9cbd999", 1, "", "AQPFvkeAQjnj6hJUMztDWPrYpy5M3H2Kgx", new BigDecimal("1407000")));
     * <p>
     * SerializeTransactionService.serializeNeoTransaction(
     * "KwnF6L1BfsMkbreUB31nqpC1KCsNB5bdqSEVaiCQnveAopaby45q",
     * "AaZa2YKVU1LrNpqnyEpsWk8xNJJvkJ4wHe",
     * "AQPFvkeAQjnj6hJUMztDWPrYpy5M3H2Kgx",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"),
     * utxos);
     *
     * @param privateKey   the privateKey of the account
     * @param from         address of the source account
     * @param to           the address of the destination account
     * @param value        the value of the transaction
     * @param fee          the fee of the transaction
     * @param genericUTXOs contains (transactionId, outputIndex, address, amount) must be requested from api provider
     * @return
     */

    public static String serializeNeoTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(Coin.NEO, privateKey, from, to, value, fee, genericUTXOs);
    }

    /**
     * This method is purposed to serialize a transaction for neo gas coins using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * List<GenericUTXO> utxos = new ArrayList<>();
     * utxos.add(new GenericUTXO("5c0f1f1904488235d77d7b0efca43ed8c7a7d61b46e1d8f47ad21e94d2a81051", 0, "", "AXL5VppKDScFX5TXyxdK4w9wnBK6WEqCrE", new BigDecimal("100000")));
     * utxos.add(new GenericUTXO("93dcf6ddf30a768bf3864642a4342025d3154600e05b3b556498e2ccb9cbd999", 1, "", "AXL5VppKDScFX5TXyxdK4w9wnBK6WEqCrE", new BigDecimal("1407000")));
     * <p>
     * SerializeTransactionService.serializeNeoGasTransaction(
     * "L5MaSxkutMja31qzTXWBZLWfxh6rpTR5avhpeeDfTBUyqQBtiWMH",
     * "AXL5VppKDScFX5TXyxdK4w9wnBK6WEqCrE",
     * "AWxx7sac8DiKWWs1GRrYZFDvng4dA8MMuX",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"),
     * utxos);
     *
     * @param privateKey   the privateKey of the account
     * @param from         address of the source account
     * @param to           the address of the destination account
     * @param value        the value of the transaction
     * @param fee          the fee of the transaction
     * @param genericUTXOs contains (transactionId, outputIndex, address, amount) must be requested from api provider
     * @return
     */

    public static String serializeNeoGasTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(Coin.GAS, privateKey, from, to, value, fee, genericUTXOs);
    }

    /**
     * This method is purposed to serialize a transaction for qtum coins using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * List<GenericUTXO> utxos = new ArrayList<>();
     * utxos.add(new GenericUTXO("5c0f1f1904488235d77d7b0efca43ed8c7a7d61b46e1d8f47ad21e94d2a81051", 0, "", "QRedvusc5T7kR5k5ekXGgjrJbAUnShK6cW", new BigDecimal("100000")));
     * utxos.add(new GenericUTXO("93dcf6ddf30a768bf3864642a4342025d3154600e05b3b556498e2ccb9cbd999", 1, "", "QRedvusc5T7kR5k5ekXGgjrJbAUnShK6cW", new BigDecimal("1407000")));
     * <p>
     * SerializeTransactionService.serializeQtumTransaction(
     * "5JybTDzJa8kBrwGRL4c3gHeH941a8Avn1Gvm1Xr6TpkaYrGaZEJ",
     * "QRedvusc5T7kR5k5ekXGgjrJbAUnShK6cW",
     * "QXv63K1NFEA8zRhBn2UTR8BVrn3YuFzKMv",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"),
     * utxos);
     *
     * @param privateKey   the privateKey of the account
     * @param from         address of the source account
     * @param to           the address of the destination account
     * @param value        the value of the transaction
     * @param fee          the fee of the transaction
     * @param genericUTXOs contains (transactionId, outputIndex, address, amount) must be requested from api provider
     * @return
     */

    public static String serializeQtumTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs) {
        return serializeBitcoinLikeTransaction(Coin.QTUM, privateKey, from, to, value, fee, genericUTXOs);
    }

    /**
     * This method is purposed to serialize a transaction for bitcoin-like coins (Bitcoin, Litecoin, Dogecoin, Dash, BitcoinCash, BitcoinGold, BitcoinSV, ZCash) using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * SerializeTransactionService.serializeRippleTransaction(
     * Coin.DASH,
     * "7qhmYq2hRQAWk4vGLNpvKX6bdSzRpofityB25qKm2mmWW8sxcGS",
     * "Xg3nrBxGRfQQL8nqYHDV722S1JcNGGRhvK",
     * "XbtvuiCxf6nz5Bd6ziufniViuLUbXdT3aq",
     * new BigDecimal("1000000"),
     * new BigDecimal("27500"),
     * new BigDecimal("1"));
     *
     * @param privateKey the privateKey of the account
     * @param from       address of the source account
     * @param to         the address of the destination account
     * @param value      the value of the transaction
     * @param fee        the fee of the transaction
     * @param txNCount   the count of transactions of the account that must be requested from api provider
     * @return
     */

    public static String serializeRippleTransaction(Coin coin, String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal txNCount) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", txNCount.toBigInteger());
        return CryptoSdkService.serializeTransaction(coin, privateKey, from, to, value, fee, values);
    }

    /**
     * This method is purposed to serialize a transaction for ethereum-like coins using given parameters
     * Note that values and amounts are supposed to be in ether
     * <p>
     * Sample:
     * SerializeTransactionService.serializeEthereumTransaction(
     * "5b53eed1209949fb5ff755bd257f5391ccaa2a27aef0e4d51906abb6d06adabb",
     * "0xEE1ab604E3c41c9A96C66425fACd4f592a00F121",
     * "0xc96BeeFa66029eD46E8435e96039CAa2E0d3c18a",
     * new BigDecimal("1000000"),
     * new BigDecimal("27500"),
     * new BigDecimal("11"))
     *
     * @param privateKey the privateKey of the account
     * @param from       address of the source account
     * @param to         the address of the destination account
     * @param value      the value of the transaction
     * @param fee        the fee of the transaction
     * @param nonce      the count of transactions of the account that must be requested from api provider
     * @return
     */

    public static String serializeEthereumTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.ETHEREUM, privateKey, from, to, EthereumUtils.convertEtherToWei(value), EthereumUtils.convertEtherToWei(fee), values);
    }

    public static String serializeEthereumClassicTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.ETHEREUM_CLASSIC, privateKey, from, to, EthereumUtils.convertEtherToWei(value), EthereumUtils.convertEtherToWei(fee), values);
    }

    public static String serialize0xTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin._0x, privateKey, from, to, value, fee, values);
    }

    public static String serializeAeternityTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.AETERNITY, privateKey, from, to, value, fee, values);
    }

    public static String serializeAugurTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.AUGUR, privateKey, from, to, value, fee, values);
    }

    public static String serializeBasicAttentionTokenTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.BASIC_ATTENTIOON_TOKEN, privateKey, from, to, value, fee, values);
    }

    public static String serializeBinanceCoinTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.BINANCECOIN, privateKey, from, to, value, fee, values);
    }

    public static String serializeChainLinkTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.CHAINLINK, privateKey, from, to, value, fee, values);
    }

    public static String serializeMultiCollateralDaiTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.MULTI_COLLATERAL_DAI, privateKey, from, to, value, fee, values);
    }

    public static String serializeEOSTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.EOS, privateKey, from, to, value, fee, values);
    }

    public static String serializeGolemTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.GOLEM, privateKey, from, to, value, fee, values);
    }

    public static String serializeMakerTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.MAKER, privateKey, from, to, value, fee, values);
    }

    public static String serializerOmiseGoTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.OMISEGO, privateKey, from, to, value, fee, values);
    }

    public static String serializerSaiTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.SAI, privateKey, from, to, value, fee, values);
    }

    public static String serializerStatusTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.STATUS, privateKey, from, to, value, fee, values);
    }

    public static String serializerZilliqaTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal nonce) {
        Map<String, Object> values = new HashMap<>();

        values.put("txn-count", nonce.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.ZILLIQA, privateKey, from, to, value, fee, values);
    }

    /**
     * This method is purposed to serialize a transaction for lisk coins using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * <p>
     * SerializeTransactionService.serializeLiskTransaction(
     * Coin.DASH,
     * "e864002835604c13fe0ee3b0a8276144569ec2b5c0db29bb3e803c37ebb63d0e",
     * "Ae2tdPwUPEYzHq1S5bZ3ZnLLUzxprj3VMn9u8yD5qxSg4YmhByXhcunMKk2",
     * "Ae2tdPwUPEZAy6aqXZmUKV3uHRXdpSGa5Eb3rm2UyjgYUGE4mXP73iuyEK5",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"));
     *
     * @param privateKey the privateKey of the account
     * @param from       address of the source account
     * @param to         the address of the destination account
     * @param value      the value of the transaction
     * @param fee        the fee of the transaction
     * @return
     */

    public static String serializeLiskTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee) {
        return CryptoSdkService.serializeTransaction(Coin.LISK, privateKey, from, to, value, fee, new HashMap<>());
    }

    /**
     * This method is purposed to serialize a transaction for waves coins using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * <p>
     * SerializeTransactionService.serializeWavesTransaction(
     * Coin.DASH,
     * "ESAdGiaQLtA9WKP4YBCMZGR3kjeAiCis2d7D3p71bppG",
     * "3PM7QCm6N7iBFTu5AgZScTyZoNqUcqdoLD7",
     * "3PECEQ9i2p4E9wRpVpHXfVtBQ1sf84uorBy",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"));
     *
     * @param privateKey the privateKey of the account
     * @param from       address of the source account
     * @param to         the address of the destination account
     * @param value      the value of the transaction
     * @param fee        the fee of the transaction
     * @return
     */

    public static String serializeWavesTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee) {
        return CryptoSdkService.serializeTransaction(Coin.LISK, privateKey, from, to, value, fee, new HashMap<>());
    }

    /**
     * This method is purposed to serialize a transaction for nano
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * List<GenericUTXO> utxos = new ArrayList<>();
     * utxos.add(new GenericUTXO("5c0f1f1904488235d77d7b0efca43ed8c7a7d61b46e1d8f47ad21e94d2a81051", 0, "", "xrb_3pshgg6m7ppb468cbznxckeeo7g8r1jiqz3ijz1bue3p8rkms3fu3b4zcgc6", new BigDecimal("100000")));
     * utxos.add(new GenericUTXO("93dcf6ddf30a768bf3864642a4342025d3154600e05b3b556498e2ccb9cbd999", 1, "", "xrb_3pshgg6m7ppb468cbznxckeeo7g8r1jiqz3ijz1bue3p8rkms3fu3b4zcgc6", new BigDecimal("1407000")));
     * <p>
     * SerializeTransactionService.serializeNanoTransaction(
     * "5b9d3d631f9815e2ac6358dace78bdbe1f5865903fdb395943da06c9735128c2",
     * "xrb_3pshgg6m7ppb468cbznxckeeo7g8r1jiqz3ijz1bue3p8rkms3fu3b4zcgc6",
     * "xrb_3bjd1yjdz5omcpfs7dh6rucoqx54upfy7a599pjrraeje4wtrf59tgyi4yxw",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"),
     * utxos);
     *
     * @param privateKey   the privateKey of the account
     * @param from         address of the source account
     * @param to           the address of the destination account
     * @param value        the value of the transaction
     * @param fee          the fee of the transaction
     * @param genericUTXOs contains (transactionId, outputIndex, address, amount) must be requested from api provider
     * @param work         byte array of work
     * @return
     */

    public static String serializeNanoTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, List<GenericUTXO> genericUTXOs, byte[] work) {
        Map<String, Object> values = new HashMap<>();

        dict[] utxos = new dict[genericUTXOs.size()];
        for (int index = 0; index < genericUTXOs.size(); index++) {
            validateGenericUtxoParameters(genericUTXOs.get(index));
            GenericUTXO utxo = genericUTXOs.get(index);
            dict utxoDict = new dict();
            utxoDict.put("txnid", utxo.getTransactionHash());
            utxoDict.put("index", BigInteger.valueOf(utxo.getTransactionIndex()));
            utxoDict.put("address", utxo.getAddress());
            utxoDict.put("amount", utxo.getValue().toBigInteger());
            utxos[index] = utxoDict;
        }

        values.put("utxos", utxos);
        values.put("work", work);
        return CryptoSdkService.serializeTransaction(Coin.NANO, privateKey, from, to, value, fee, values);
    }

    /**
     * This method is purposed to serialize a transaction for stellar coins using given parameters
     * Note that values and amounts are supposed to be in bitcoins
     * <p>
     * Sample:
     * <p>
     * SerializeTransactionService.serializeStellarTransaction(
     * "SBCZ45MI7PADILNIRDHTNDDTWE3SPTMHPOZOE7ULDYI2SFMZKTOOV5HS",
     * "GBL63HZWVNOPU2FKV3N2SESMKY4GEPIIV4OIR4PHOLRZSCRCCEB6WUEB",
     * "GBZWG5BLKZAKREPWSJVOO5NWQJR3YTIGWQQX2GRK2EN5ZLVVQGNB52KE",
     * new BigDecimal("100000"),
     * new BigDecimal("2750"),
     * new BigDecimal("12"),
     * new BigDecimal("21222"));
     *
     * @param privateKey the privateKey of the account
     * @param from       address of the source account
     * @param to         the address of the destination account
     * @param value      the value of the transaction
     * @param fee        the fee of the transaction
     * @param txNCount   the transaction count of the account that must be requested from api provider
     * @param balance    balance of the account
     * @return
     */

    public static String serializeStellarTransaction(String privateKey, String from, String to, BigDecimal value, BigDecimal fee, BigDecimal txNCount, BigDecimal balance) {
        Map<String, Object> values = new HashMap<>();
        values.put("txn_count", txNCount.toBigInteger());
        values.put("balance", balance.toBigInteger());
        return CryptoSdkService.serializeTransaction(Coin.STELLAR, privateKey, from, to, value, fee, values);
    }

    private static void validateGenericUtxoParameters(GenericUTXO utxo) {
        Objects.requireNonNull(utxo, "UTXO should not be null!");
        Objects.requireNonNull(utxo.getTransactionIndex(), "the index of UTXO should not be null!");
        Objects.requireNonNull(utxo.getTransactionHash(), "the transaction hash of UTXO should not be null!");
        Objects.requireNonNull(utxo.getValue(), "the value of UTXO should not be null!");
        Objects.requireNonNull(utxo.getAddress(), "the address of UTXO should not be null!");
    }

    private static List<GenericUTXO> processUTXOs(List<GenericUTXO> genericUTXOs, ValueProcessor processor) {
        for (GenericUTXO genericUTXO : genericUTXOs) {
            genericUTXO.setValue(processor.process(genericUTXO.getValue()));
        }
        return genericUTXOs;
    }

    private interface ValueProcessor {
        BigDecimal process(BigDecimal value);
    }

}
