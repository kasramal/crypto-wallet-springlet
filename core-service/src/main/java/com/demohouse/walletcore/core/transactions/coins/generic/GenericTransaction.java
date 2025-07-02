package com.demohouse.walletcore.core.transactions.coins.generic;

import com.demohouse.walletcore.core.addresses.legacy.WifGenerator;
import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import com.demohouse.walletcore.utils.signature.ECSignUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericTransaction /*implements Transaction */{


    private static final int VERSION = 1;
    private static final String SIGWIT_FLAG = "0001";
    protected static final int DEFAULT_LOCK_TIME = 0;
    protected static final String DEFAULT_SEQUENCE = "ffffffff";
    protected static final int SIGNATURE_HASH_CODE_TYPE = 1;
    protected static final Logger logger = Logger.getLogger(GenericTransaction.class);
    private final boolean sigwitPresent = false;
    private String fromAddress;
    private String toAddress;
    private List<GenericTransactionInput> inputs = new ArrayList<>();
    private List<GenericTransactionOutput> outputs = new ArrayList<>();

    public static String hash(String txString) {
        return WalletHashUtils.doubleSha256(txString);
    }

    public long getSignatureHashCodeType() {
        return SIGNATURE_HASH_CODE_TYPE;
    }

    public String hashUnsignedTransaction(int signingInputIndex) {
        return GenericTransaction.hash(serializeUnsignedTransaction(signingInputIndex));
    }

    public long getVersion() {
        return VERSION;
    }

    public abstract BigDecimal convertValue(BigDecimal value);

    public String serializeOutputs() {
        StringBuilder outputs = new StringBuilder();
        for (int index = 0; index < getOutputs().size(); index++) {
            GenericTransactionOutput output = getOutputs().get(index);
            outputs.append(WalletHexUtils.longToHex(convertValue(output.getValue()).longValue(), 8, true));
            outputs.append(WalletHexUtils.longToHex(output.getPubKeyScript().length() / 2, true));
            outputs.append(output.getPubKeyScript());
        }
        return outputs.toString();
    }

    public String serializeUnsignedTransaction(int signingInputIndex) {
        StringBuilder rawTransactionInHex = new StringBuilder();
        rawTransactionInHex.append(WalletHexUtils.longToHex(getVersion(), 4, true));

        if (sigwitPresent) rawTransactionInHex.append(SIGWIT_FLAG);

        rawTransactionInHex.append(WalletHexUtils.longToHex(getInputs().size()));
        for (int index = 0; index < getInputs().size(); index++) {
            GenericTransactionInput input = getInputs().get(index);

            rawTransactionInHex.append(input.getHashLittleEndian());
            rawTransactionInHex.append(WalletHexUtils.longToHex(input.getIndex(), 4, true));
            if (index == signingInputIndex) {
                rawTransactionInHex.append(WalletHexUtils.longToHex(input.getPubKeyScript().length() / 2, true));
                rawTransactionInHex.append(input.getPubKeyScript());

            } else {
                rawTransactionInHex.append(WalletHexUtils.longToHex(0));
            }
            rawTransactionInHex.append(DEFAULT_SEQUENCE);
        }

        rawTransactionInHex.append(WalletHexUtils.longToHex(getOutputs().size()));

        rawTransactionInHex.append(serializeOutputs());

        rawTransactionInHex.append(WalletHexUtils.longToHex(DEFAULT_LOCK_TIME, 4, true));
        rawTransactionInHex.append(WalletHexUtils.longToHex(getSignatureHashCodeType(), 4, true));

        logger.debug(MessageFormat.format("Unsigned tx raw {0}: {1}", signingInputIndex, rawTransactionInHex.toString()));
        return rawTransactionInHex.toString();
    }

    private String sign(String unsignedHexTransaction, String privateKey) {
        String signature = ECSignUtils.signScript(unsignedHexTransaction, privateKey);
        return GenericUtils.generateScriptSig(signature, privateKey, getSignatureHashCodeType());
    }

    /**
     * A minimum length transaction is supposed to contain a single input and a single output.
     * An input consists of 146 Bytes in average.
     * An output consists of 33 Bytes in average.
     * A transaction consists of 10 constant byte in addition to input and output data.
     * So that a MIN_TX_LENGTH is estimated such below:
     */
    public int estimateTransactionLength() {
        return getInputs().size() * 146 + getOutputs().size() * 33 + 10;
    }

    public String serialize(String wif) {
        StringBuilder rawTransactionInHex = new StringBuilder();
        rawTransactionInHex.append(WalletHexUtils.longToHex(getVersion(), 4, true));
        logger.debug(MessageFormat.format("Version: {0}", WalletHexUtils.longToHex(getVersion(), 4, true)));

        if (sigwitPresent) rawTransactionInHex.append(SIGWIT_FLAG);

        rawTransactionInHex.append(WalletHexUtils.longToHex(getInputs().size()));
        logger.debug(MessageFormat.format("Inputs: {0}", WalletHexUtils.longToHex(getInputs().size())));
        for (int index = 0; index < getInputs().size(); index++) {
            GenericTransactionInput input = getInputs().get(index);

            rawTransactionInHex.append(input.getHashLittleEndian());
            logger.debug(MessageFormat.format("Hash: {0}", input.getHashLittleEndian()));
            rawTransactionInHex.append(WalletHexUtils.longToHex(input.getIndex(), 4, true));
            logger.debug(MessageFormat.format("index: {0}", WalletHexUtils.longToHex(input.getIndex(), 4, true)));

            String privateKey = WifGenerator.convertWifToPrivateKey(wif);
            String signedScript = sign(hashUnsignedTransaction(index), privateKey);
            rawTransactionInHex.append(WalletHexUtils.longToHex(signedScript.length() / 2, true));
            logger.debug(MessageFormat.format("signedScriptLength: {0}", WalletHexUtils.longToHex(signedScript.length() / 2, true)));
            rawTransactionInHex.append(signedScript);
            logger.debug(MessageFormat.format("signedScript: {0}", signedScript));
            rawTransactionInHex.append(DEFAULT_SEQUENCE);
            logger.debug(MessageFormat.format("sequence: {0}", DEFAULT_SEQUENCE));

        }
        rawTransactionInHex.append(WalletHexUtils.longToHex(getOutputs().size()));
        logger.debug(MessageFormat.format("outputs: {0}", WalletHexUtils.longToHex(getOutputs().size())));
        for (int index = 0; index < getOutputs().size(); index++) {
            GenericTransactionOutput output = getOutputs().get(index);

            rawTransactionInHex.append(WalletHexUtils.longToHex(convertValue(output.getValue()).longValue(), 8, true));
            logger.debug(MessageFormat.format("value: {0}", WalletHexUtils.longToHex(convertValue(output.getValue()).longValue(), 8, true)));
            rawTransactionInHex.append(WalletHexUtils.longToHex(output.getPubKeyScript().length() / 2, true));
            logger.debug(MessageFormat.format("outputPublicKeyScriptLength: {0}", WalletHexUtils.longToHex(output.getPubKeyScript().length() / 2, true)));
            rawTransactionInHex.append(output.getPubKeyScript());
            logger.debug(MessageFormat.format("outputPublicKeyScript: {0}", output.getPubKeyScript()));
        }

        rawTransactionInHex.append(WalletHexUtils.longToHex(DEFAULT_LOCK_TIME, 4, true));
        logger.debug(MessageFormat.format("LOCK_TIME: {0}", WalletHexUtils.longToHex(DEFAULT_LOCK_TIME, 4, true)));

        logger.debug(MessageFormat.format("Signed tx raw: {0}", rawTransactionInHex.toString()));
        return rawTransactionInHex.toString();
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public List<GenericTransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<GenericTransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<GenericTransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<GenericTransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public void addTransactionInput(GenericTransactionInput input) {
        inputs.add(input);
    }

    public void addTransactionOutput(GenericTransactionOutput output) {
        outputs.add(output);
    }
}
