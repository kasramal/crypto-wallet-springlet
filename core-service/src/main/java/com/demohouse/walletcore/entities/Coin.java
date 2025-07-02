package com.demohouse.walletcore.entities;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.transactions.coins.bitcoin.BitcoinUtils;
import com.demohouse.walletcore.core.transactions.coins.bitcoincash.BitcoinCashUtils;
import com.demohouse.walletcore.core.transactions.coins.bitcoinsv.BitcoinSVUtils;
import com.demohouse.walletcore.core.transactions.coins.dash.DashUtils;
import com.demohouse.walletcore.core.transactions.coins.dogecoin.DogecoinUtils;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.tether.TetherUtils;
import com.demohouse.walletcore.core.transactions.coins.litecoin.LitecoinUtils;
import com.demohouse.walletcore.core.transactions.coins.tezos.TezosUtils;
import com.demohouse.walletcore.core.transactions.coins.tron.TronUtils;
import com.demohouse.walletcore.core.transactions.coins.zcash.ZcashUtils;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Coin {
    NOT_SUPPORTED("", "NSP", 0, 0, ""),
    RIAL("rial", "IRR", 0, 0, ""),
    BITCOIN("bitcoin", "BTC", 8, 8, "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"),
    BITCOINSV("bitcoinsv", "BSV", 8, 8, "https://cdn.coinranking.com/388ehh6kq/bitcoin-sv-1.svg"),
    BITCOINCASH("bitcoincash", "BCH", 8, 8, "https://cdn.coinranking.com/By8ziihX7/bch.svg"),
    LITECOIN("litecoin", "LTC", 8, 8, "https://cdn.coinranking.com/BUvPxmc9o/ltcnew.svg"),
    RIPPLE("ripple", "XRP", 8, 8, "https://cdn.coinranking.com/B1oPuTyfX/xrp.svg"),
    DASH("dash", "DASH", 8, 8, "https://cdn.coinranking.com/PyCmduSxt/Dash-D-white_on_blue_circle.svg"),
    ZCASH("zcash", "ZEC", 8, 8, "https://cdn.coinranking.com/rJrKiS_uZ/zec.svg"),
    DOGECOIN("dogecoin", "DOGE", 8, 8, "https://cdn.coinranking.com/H1arXIuOZ/doge.svg"),
    TETHER("tether", "USDT", 8, 8, "https://cdn.coinranking.com/mgHqwlCLj/usdt.svg", true, "0xdac17f958d2ee523a2206206994597c13d831ec7"),
    ETHEREUM("ethereum", "ETH", 8, 10, "https://cdn.coinranking.com/rk4RKHOuW/eth.svg"),
    ETHEREUM_CLASSIC("ethereumclassic", "ETC", 8, 10, "https://cdn.coinranking.com/rJfyor__W/etc.svg"),
    TRON("tron", "TRX", 10, 8, "https://cdn.coinranking.com/behejNqQs/trx.svg"),
    TEZOS("tezos", "XTZ", 10, 8, "https://cdn.coinranking.com/HkLUdilQ7/xtz.svg"),

    CARDANO("cardano", "BTG", 10, 10, "https://cdn.coinranking.com/ryY28nXhW/ada.svg"),
    BITCOINGOLD("bitcoingold", "BTG", 10, 10, "https://cdn.coinranking.com/BkLedkw-G/btg.svg"),
    DECRED("decred", "DCR", 10, 10, "https://cdn.coinranking.com/SyJaBU_dZ/dcr.svg"),
    DIGIBYTE("digibyte", "DGB", 10, 10, "https://cdn.coinranking.com/HJcoNFDSM/dgb.svg"),
    LISK("lisk", "LSK", 10, 10, "https://cdn.coinranking.com/SJTGUIozm/lisk.svg"),
    NANO("nano", "NANO", 10, 10, "https://cdn.coinranking.com/HygqO-iPM/nano.svg"),
    NEO("neo", "NEO", 10, 10, "https://cdn.coinranking.com/MgUNVQCeN/neo.svg"),
    GAS("neogas", "GAS", 10, 10, "https://cdn.coinranking.com/rJMSO8ddb/neo.svg"),
    QTUM("qtum", "QTUM", 10, 10, "https://cdn.coinranking.com/TByBVzFNq/qtum.svg"),
    STELLAR("stellar", "XLM", 10, 10, "https://cdn.coinranking.com/78CxK1xsp/Stellar_symbol_black_RGB.svg"),
    WAVES("waves", "WAVES", 10, 10, "https://cdn.coinranking.com/B1tzRbyMz/waves.svg"),
    _0x("0x", "ZRX", 10, 10, "https://cdn.coinranking.com/Hy7oqBSFW/zrx.svg"),
    AETERNITY("aeternity", "AE", 10, 10, "https://cdn.coinranking.com/HJg3pANTZ/ae.svg"),
    AUGUR("augur", "REP", 10, 10, "https://cdn.coinranking.com/H1GlZLd_Z/rep.svg"),
    BASIC_ATTENTIOON_TOKEN("basicattentiontoken", "BAT", 10, 10, "https://cdn.coinranking.com/SyX3N05JQ/bat.svg"),
    BINANCECOIN("binancecoin", "BNB", 10, 10, "https://cdn.coinranking.com/B1N19L_dZ/bnb.svg"),
    CHAINLINK("chainlink", "LINK", 10, 10, "https://cdn.coinranking.com/9NOP9tOem/chainlink.svg"),
    MULTI_COLLATERAL_DAI("dai", "DAI", 10, 10, "https://cdn.coinranking.com/mAZ_7LwOE/mutli-collateral-dai.svg"),
    EOS("eos", "EOS", 10, 10, "https://cdn.coinranking.com/PqOYrWSje/eos2.svg"),
    GOLEM("golem", "GNT", 10, 10, "https://cdn.coinranking.com/rkzb-Uuu-/gnt.svg"),
    MAKER("maker", "MKR", 10, 10, "https://cdn.coinranking.com/sjHfS7jCS/mkrdao.svg"),
    OMISEGO("omisego", "OMG", 10, 10, "https://cdn.coinranking.com/BJGUoHu_Z/omg.svg"),
    SAI("sai", "SAI", 10, 10, "https://cdn.coinranking.com/dmbwGK2xSg/sai.svg"),
    STATUS("status", "SNT", 10, 10, "https://cdn.coinranking.com/B1MQMP__Z/snt.svg"),
    ZILLIQA("zilliqa", "ZIL", 10, 10, "https://cdn.coinranking.com/H18JFWswf/zil.svg");


    private final String name;
    private final String iso;
    private final int count;
    private final int precision;
    private final String logo;
    private final boolean secondLayer;
    private final String contractId;

    Coin(String name, String iso, int count, int precision, String logo) {
        this(name, iso, count, precision, logo, false, null);
    }

    Coin(String name, String iso, int count, int precision, String logo, boolean secondLayer, String contractId) {
        this.name = name;
        this.iso = iso;
        this.precision = precision;
        this.count = count;
        this.logo = logo;
        this.secondLayer = secondLayer;
        this.contractId = contractId;
    }

    public static Coin findByIso(String iso) {
        for (Coin v : values()) {
            if (v.getIso().equals(iso)) {
                return v;
            }
        }
        throw new CryptoCurrencyApiException(CryptoCurrencyError.COIN_NOT_SUPPORTED);
    }

    public static List<String> isoList() {
        List<String> isoList = new ArrayList<>();
        for (Coin v : values()) {
            isoList.add(v.getIso());
        }
        return isoList;
    }

    public static List<Coin> excludedCoins() {
        return Arrays.asList(
                NOT_SUPPORTED,
                RIAL,
                RIPPLE,
                CARDANO,
                BITCOINGOLD,
                DECRED,
                DIGIBYTE,
                LISK,
                NANO,
                NEO,
                GAS,
                QTUM,
                STELLAR,
                WAVES,
                _0x,
                AETERNITY,
                AUGUR,
                BASIC_ATTENTIOON_TOKEN,
                BINANCECOIN,
                CHAINLINK,
                MULTI_COLLATERAL_DAI,
                EOS,
                GOLEM,
                MAKER,
                OMISEGO,
                SAI,
                STATUS,
                ZILLIQA
        );
    }

    public static List<Coin> secondLayerValues() {
        return Collections.singletonList(TETHER);
    }

    public BigDecimal convertValue(BigDecimal value) {
        switch (this) {
            case BITCOIN:
                return BitcoinUtils.satoshiToBitcoin(value);
            case BITCOINCASH:
                return BitcoinCashUtils.satoshiToBitcoinCash(value);
            case BITCOINSV:
                return BitcoinSVUtils.satoshiToBitcoinSV(value);
            case DASH:
                return DashUtils.satoshiToDash(value);
            case ZCASH:
                return ZcashUtils.satoshiToZcash(value);
            case LITECOIN:
                return LitecoinUtils.satoshiToLitecoin(value);
            case DOGECOIN:
                return DogecoinUtils.satoshiToDogecoin(value);
            case ETHEREUM:
            case ETHEREUM_CLASSIC:
                return EthereumUtils.convertWeiToEther(value);
            case TRON:
                return TronUtils.sunToTrx(value);
            case TEZOS:
                return TezosUtils.microXTZtoXTZ(value);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal convertMacroToMicroValue(BigDecimal value) {
        switch (this) {
            case BITCOIN:
                return BitcoinUtils.bitcoinToSatoshi(value);
            case BITCOINCASH:
                return BitcoinCashUtils.bitcoinCashToSatoshi(value);
            case BITCOINSV:
                return BitcoinSVUtils.bitcoinSVToSatoshi(value);
            case DASH:
                return DashUtils.dashToSatoshi(value);
            case ZCASH:
                return ZcashUtils.zcashToSatoshi(value);
            case LITECOIN:
                return LitecoinUtils.litecoinToSatoshi(value);
            case DOGECOIN:
                return DogecoinUtils.dogecoinToSatoshi(value);
            case ETHEREUM:
            case ETHEREUM_CLASSIC:
                return EthereumUtils.convertEtherToWei(value);
            case TETHER:
                return TetherUtils.convertUSDTToTether(value);
            case TRON:
                return TronUtils.trxToSun(value);
            case TEZOS:
                return TezosUtils.xtzToMicroXTZ(value);
        }
        return BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public String getIso() {
        return iso;
    }

    public int getCount() {
        return count;
    }

    public int getPrecision() {
        return precision;
    }

    public String getLogo() {
        return logo;
    }

    public List<CryptoCurrencyApiProvider> getApiProviders() {
        switch (this) {
            case RIAL:
            case NOT_SUPPORTED:
                return Collections.emptyList();
            case BITCOIN:
                return Arrays.asList(
                        CryptoCurrencyApiProvider.BLOCK_CHAIR,
                        CryptoCurrencyApiProvider.BLOCK_CHAIN,
                        CryptoCurrencyApiProvider.BLOCK_CYPHER,
                        CryptoCurrencyApiProvider.CRYPTO_APIS
                );
            case BITCOINCASH:
                return Arrays.asList(
                        CryptoCurrencyApiProvider.BLOCK_CHAIR,
                        CryptoCurrencyApiProvider.CRYPTO_APIS
                );
            case ETHEREUM:
            case DOGECOIN:
            case LITECOIN:
                return Arrays.asList(
                        CryptoCurrencyApiProvider.BLOCK_CHAIR,
                        CryptoCurrencyApiProvider.BLOCK_CYPHER,
                        CryptoCurrencyApiProvider.CRYPTO_APIS
                );

            case ETHEREUM_CLASSIC:
                return Collections.singletonList(CryptoCurrencyApiProvider.CRYPTO_APIS);
            case TRON:
                return Collections.singletonList(CryptoCurrencyApiProvider.TRON_PROVIDER);
            case TEZOS:
                return Collections.singletonList(CryptoCurrencyApiProvider.CRYPTO_NOMIC);
            case DASH:
            case ZCASH:
            case BITCOINSV:
            case TETHER:
            case RIPPLE:
            default:
                return Collections.singletonList(CryptoCurrencyApiProvider.BLOCK_CHAIR);
        }
    }

    public Coin getBaseCoin() {
        switch (this) {
            case TETHER:
                return ETHEREUM;
            default:
                return null;
        }
    }

    public boolean isSecondLayer() {
        return secondLayer;
    }

    public String getContractId() {
        return contractId != null ? contractId : "";
    }

    public String getNameKey() {
        return "coin.name." + this;
    }
}
