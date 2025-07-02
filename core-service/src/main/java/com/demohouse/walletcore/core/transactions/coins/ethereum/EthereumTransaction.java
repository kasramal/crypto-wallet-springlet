package com.demohouse.walletcore.core.transactions.coins.ethereum;

import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECDSASignatureDetails;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EthereumTransaction {

    private String toAddress;
    private BigDecimal nonce;
    private BigDecimal gasPrice;
    private BigDecimal gasLimit;
    private BigDecimal fee;
    private BigDecimal amount;
    private byte[] data;

    public RawTransaction createRawTx() {
        return RawTransaction
                .createEtherTransaction(
                        nonce.toBigInteger(),
                        EthereumUtils.convertEtherToWei(gasPrice).toBigInteger(),
                        gasLimit.toBigInteger(),
                        toAddress,
                        EthereumUtils.convertEtherToWei(amount).toBigInteger());
    }

    public List<RlpType> serialize(ECDSASignatureDetails signature) {
        List<RlpType> result = new ArrayList<>();
        result.add(RlpString.create(nonce.toBigInteger()));
        result.add(RlpString.create(EthereumUtils.convertEtherToWei(gasPrice).toBigInteger()));
        result.add(RlpString.create(gasLimit.toBigInteger()));
        if (toAddress != null && toAddress.length() > 0) {
            result.add(RlpString.create(WalletHexUtils.decodeHexString(toAddress.replace("0x", "").toLowerCase())));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(EthereumUtils.convertEtherToWei(amount).longValue()));
        result.add(RlpString.create(data));

        if (signature != null) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getS())));
        }
        return result;
    }

    public byte[] signUsingWeb3j(String privateKey, RawTransaction txRaw) {
        Credentials credentials = Credentials.create(privateKey);
        return TransactionEncoder.signMessage(txRaw, credentials);
    }

    public String sign(String privateKey, boolean useWeb3J) {
        if (useWeb3J) {
            RawTransaction txRaw = createRawTx();
            return WalletHexUtils.bytesToHex(signUsingWeb3j(privateKey, txRaw));
        }
        return sign(privateKey);
    }

    private String sign(String privateKey) {
        byte[] unsigned = RlpEncoder.encode(new RlpList(serialize(null)));
        ECDSASignatureDetails signature = EthereumUtils.signRawTx(unsigned, privateKey);
        byte[] signed = RlpEncoder.encode(new RlpList(serialize(signature)));
        return WalletHexUtils.bytesToHex(signed);
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getNonce() {
        return nonce;
    }

    public void setNonce(BigDecimal nonce) {
        this.nonce = nonce;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigDecimal getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigDecimal gasLimit) {
        this.gasLimit = gasLimit;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
