package com.demohouse.walletcore.core.transactions.coins.ethereum.erc20;

import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumTransaction;
import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECDSASignatureDetails;
import org.web3j.crypto.RawTransaction;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;

import java.util.ArrayList;
import java.util.List;

public class Erc20Transaction extends EthereumTransaction {

    private String contractId;
    private String encodedData;

    @Override
    public RawTransaction createRawTx() {
        return RawTransaction
                .createTransaction(
                        getNonce().toBigInteger(),
                        EthereumUtils.convertEtherToWei(getGasPrice()).toBigInteger(),
                        getGasLimit().toBigInteger(),
                        contractId,
                        encodedData);
    }

    @Override
    public List<RlpType> serialize(ECDSASignatureDetails signature) {
        List<RlpType> result = new ArrayList<>();
        result.add(RlpString.create(getNonce().toBigInteger()));
        result.add(RlpString.create(EthereumUtils.convertEtherToWei(getGasPrice()).toBigInteger()));
        result.add(RlpString.create(getGasLimit().toBigInteger()));
        if (contractId != null && contractId.length() > 0) {
            result.add(RlpString.create(WalletHexUtils.decodeHexString(contractId.replace("0x", "").toLowerCase())));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(EthereumUtils.convertEtherToWei(getAmount()).longValue()));
        result.add(RlpString.create(encodedData));

        if (null != signature) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getS())));
        }
        return result;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getEncodedData() {
        return encodedData;
    }

    public void setEncodedData(String encodedData) {
        this.encodedData = encodedData;
    }
}
