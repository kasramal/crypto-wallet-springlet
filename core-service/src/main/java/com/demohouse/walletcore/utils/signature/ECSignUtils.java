package com.demohouse.walletcore.utils.signature;

import com.demohouse.walletcore.utils.WalletHashUtils;
import com.demohouse.walletcore.utils.WalletHexUtils;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import static com.demohouse.walletcore.utils.AssertionUtils.assertThat;

public class ECSignUtils {

    public static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    public static final ECDomainParameters CURVE;
    public static final BigInteger HALF_CURVE_ORDER;

    static {
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
        HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);
        Security.addProvider(new BouncyCastleProvider());
    }


    public static ECKeyPair generateECKeyPair(byte[] seed) {
        return generateECKeyPair(new SecureRandom(seed));
    }

    public static ECKeyPair generateECKeyPair() {
        return generateECKeyPair(new SecureRandom());
    }

    private static ECKeyPair generateECKeyPair(SecureRandom secureRandom) {
        int length = 0;
        byte[][] keys;
        do {
            ECKeyPairGenerator gen = new ECKeyPairGenerator();

            ECKeyGenerationParameters keyGenParam = new ECKeyGenerationParameters(CURVE, secureRandom);
            gen.init(keyGenParam);
            AsymmetricCipherKeyPair kp = gen.generateKeyPair();

            ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) kp.getPrivate();
//            ECPoint dd = CURVE_PARAMS.getG().multiply(privateKey.getD());
//            byte[] publicKey = new byte[65];
//            System.arraycopy(dd.getYCoord().toBigInteger().toByteArray(), 0, publicKey, 64 - dd.getYCoord().toBigInteger().toByteArray().length + 1, dd.getYCoord().toBigInteger().toByteArray().length);
//            System.arraycopy(dd.getXCoord().toBigInteger().toByteArray(), 0, publicKey, 32 - dd.getXCoord().toBigInteger().toByteArray().length + 1, dd.getXCoord().toBigInteger().toByteArray().length);
//            publicKey[0] = 4;
            byte[] publicKey = WalletHexUtils.decodeHexString(computePublicKey(privateKey.getD().toByteArray()));
            length = privateKey.getD().toByteArray().length;
            keys = new byte[][]{privateKey.getD().toByteArray(), publicKey};
        } while (length != 32);
        return new ECKeyPair(keys[0], keys[1]);
    }


    public static String computePublicKey(String privateKey) {
        return computePublicKey(WalletHexUtils.decodeHexString(privateKey), false);
    }

    public static String computePublicKey(String privateKey, boolean isCompressed) {
        return computePublicKey(WalletHexUtils.decodeHexString(privateKey), isCompressed);
    }

    public static String computePublicKey(byte[] privateKeyBytes) {
        return computePublicKey(privateKeyBytes, false);
    }

    public static String computePublicKey(byte[] privateKeyBytes, boolean isCompressed) {
        boolean compressed = false;

        if (privateKeyBytes.length == 33 && privateKeyBytes[32] == 1) {
            compressed = true;
            privateKeyBytes = Arrays.copyOf(privateKeyBytes, 32);  // Chop off the additional marker byte.
        }
        BigInteger privKeyB = new BigInteger(1, privateKeyBytes);

        ECPoint point = ECSignUtils.CURVE.getG().multiply(privKeyB);
        if (compressed) {
            point = new ECPoint.Fp(ECSignUtils.CURVE.getCurve(), point.getXCoord(), point.getYCoord(), true);
        }

        byte[] publicKey = point.getEncoded(isCompressed);
        return WalletHexUtils.bytesToHex(publicKey);
    }

    public static String signScript(String script, String privateKey) {
        byte[] bytes;
        bytes = WalletHexUtils.decodeHexString(privateKey);
        if (bytes.length == 33 && bytes[32] == 1) {
            bytes = Arrays.copyOf(bytes, 32);  // Chop off the additional marker byte.
        }
        BigInteger privateKeyInteger = new BigInteger(1, bytes);
        byte[] message = WalletHexUtils.decodeHexString(script);
        ECDSASigner signer = new ECDSASigner();
        ECDSASignature sig = sign(signer, privateKeyInteger, message);
        return WalletHexUtils.bytesToHex(toDER(sig.r, sig.s));
    }

    private static byte[] toDER(BigInteger r, BigInteger s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(72);
        DERSequenceGenerator seq;
        byte[] res;
        try {
            seq = new DERSequenceGenerator(bos);
            seq.addObject(new DERInteger(r));
            seq.addObject(new DERInteger(s));
            seq.close();
            res = bos.toByteArray();
            return res;
        } catch (IOException ignored) {
        }
        return new byte[0];
    }

    public static ECDSASignatureDetails signMessage(byte[] message, String privateKey, boolean needToHash) {
        BigInteger privateKeyInteger = new BigInteger(privateKey, 16);
        BigInteger publicKeyInteger = publicKeyFromPrivate(privateKeyInteger);

        byte[] messageHash;
        if (needToHash) {
            messageHash = WalletHashUtils.sha3(message);
        } else {
            messageHash = message;
        }
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECDSASignature sig = sign(signer, privateKeyInteger, messageHash);
        int recId = -1;

        int headerByte;
        for (headerByte = 0; headerByte < 4; ++headerByte) {
            BigInteger k = recoverFromSignature(headerByte, sig, messageHash);
            if (k != null && k.equals(publicKeyInteger)) {
                recId = headerByte;
                break;
            }
        }

        if (recId == -1) {
            throw new RuntimeException("Could not construct a recoverable key. Are your credentials valid?");
        } else {
            headerByte = recId + 27;
            byte[] v = new byte[]{(byte) headerByte};
            byte[] r = WalletHexUtils.toBytesPadded(sig.r, 32);
            byte[] s = WalletHexUtils.toBytesPadded(sig.s, 32);
            return new ECDSASignatureDetails(v, r, s);
        }
    }

    public static BigInteger recoverFromSignature(int recId, ECDSASignature sig, byte[] message) {
        assertThat(recId >= 0, "recId must be positive");
        assertThat(sig.r.signum() >= 0, "r must be positive");
        assertThat(sig.s.signum() >= 0, "s must be positive");
        assertThat(message != null, "message cannot be null");
        BigInteger n = CURVE.getN();
        BigInteger i = BigInteger.valueOf((long) recId / 2L);
        BigInteger x = sig.r.add(i.multiply(n));
        BigInteger prime = SecP256K1Curve.q;
        if (x.compareTo(prime) >= 0) {
            return null;
        } else {
            ECPoint R = decompressKey(x, (recId & 1) == 1);
            if (!R.multiply(n).isInfinity()) {
                return null;
            } else {
                BigInteger e = new BigInteger(1, message);
                BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
                BigInteger rInv = sig.r.modInverse(n);
                BigInteger srInv = rInv.multiply(sig.s).mod(n);
                BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
                ECPoint q = ECAlgorithms.sumOfTwoMultiplies(CURVE.getG(), eInvrInv, R, srInv);
                byte[] qBytes = q.getEncoded(false);
                return new BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.length));
            }
        }
    }

    public static BigInteger publicKeyFromPrivate(BigInteger privKey) {
        ECPoint point = publicPointFromPrivate(privKey);
        byte[] encoded = point.getEncoded(false);
        return new BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.length));
    }

    private static ECPoint publicPointFromPrivate(BigInteger privKey) {
        if (privKey.bitLength() > CURVE.getN().bitLength()) {
            privKey = privKey.mod(CURVE.getN());
        }

        return (new FixedPointCombMultiplier()).multiply(CURVE.getG(), privKey);
    }

    private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte) (yBit ? 3 : 2);
        return CURVE.getCurve().decodePoint(compEnc);
    }

    private static ECDSASignature sign(ECDSASigner signer, BigInteger privateKey, byte[] transactionHash) {
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKey, CURVE);
        signer.init(true, privKey);
        BigInteger[] components = signer.generateSignature(transactionHash);
        return (new ECDSASignature(components[0], components[1])).canonicalize();
    }
}
