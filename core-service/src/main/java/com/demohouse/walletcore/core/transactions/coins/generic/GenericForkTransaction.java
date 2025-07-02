package com.demohouse.walletcore.core.transactions.coins.generic;

import com.demohouse.walletcore.utils.WalletHexUtils;

import java.text.MessageFormat;

public abstract class GenericForkTransaction extends GenericTransaction {
    private static final long SIGNATURE_FORK_ID_HASH_CODE_TYPE = 0x40;
    private static final long VERSION = 2;

    @Override
    public long getVersion() {
        return VERSION;
    }

    @Override
    public long getSignatureHashCodeType() {
        return SIGNATURE_HASH_CODE_TYPE + SIGNATURE_FORK_ID_HASH_CODE_TYPE;
    }

    private String serializePrevOutputs() {
        StringBuilder prevOutputs = new StringBuilder();
        for (int index = 0; index < getInputs().size(); index++) {
            GenericTransactionInput input = getInputs().get(index);
            prevOutputs.append(input.getHashLittleEndian());
            prevOutputs.append(WalletHexUtils.longToHex(input.getIndex(), 4, true));
        }
        return prevOutputs.toString();
    }


    private String serializeSequences() {
        StringBuilder sequences = new StringBuilder();
        for (int index = 0; index < getInputs().size(); index++) {
            sequences.append(DEFAULT_SEQUENCE);
        }
        return sequences.toString();
    }

    @Override
    public String serializeUnsignedTransaction(int signingInputIndex) {
        StringBuilder rawTransactionInHex = new StringBuilder();

        GenericTransactionInput inputToBeSigned = getInputs().get(signingInputIndex);

        rawTransactionInHex.append(WalletHexUtils.longToHex(getVersion(), 4, true));

        rawTransactionInHex.append(hash(serializePrevOutputs()));

        rawTransactionInHex.append(hash(serializeSequences()));

        rawTransactionInHex.append(inputToBeSigned.getHashLittleEndian());

        rawTransactionInHex.append(WalletHexUtils.longToHex(inputToBeSigned.getIndex(), 4, true));

        rawTransactionInHex.append(WalletHexUtils.longToHex(inputToBeSigned.getPubKeyScript().length() / 2, true));

        rawTransactionInHex.append(inputToBeSigned.getPubKeyScript());

        rawTransactionInHex.append(WalletHexUtils.longToHex(convertValue(inputToBeSigned.getValue()).longValue(), 8, true));

        rawTransactionInHex.append(DEFAULT_SEQUENCE);

        rawTransactionInHex.append(hash(serializeOutputs()));

        rawTransactionInHex.append(WalletHexUtils.longToHex(DEFAULT_LOCK_TIME, 4, true));

        rawTransactionInHex.append(WalletHexUtils.longToHex(getSignatureHashCodeType(), 4, true));

        logger.debug(MessageFormat.format("Unsigned tx raw {0}: {1}", signingInputIndex, rawTransactionInHex.toString()));
        return rawTransactionInHex.toString();
    }
}
