package com.demohouse.walletcore.utils.signature;

import java.math.BigInteger;

public class ECDSASignature {
    public final BigInteger r;
    public final BigInteger s;

    public ECDSASignature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    public boolean isCanonical() {
        return this.s.compareTo(ECSignUtils.HALF_CURVE_ORDER) <= 0;
    }

    public ECDSASignature canonicalize() {
        return !this.isCanonical() ? new ECDSASignature(this.r, ECSignUtils.CURVE.getN().subtract(this.s)) : this;
    }
}
