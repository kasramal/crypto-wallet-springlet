package com.demohouse.walletcore.utils.signature;

public class ECDSASignatureDetails {
    private byte[] v;
    private byte[] r;
    private byte[] s;

    public ECDSASignatureDetails(byte[] v, byte[] r, byte[] s) {
        this.v = v;
        this.r = r;
        this.s = s;
    }

    public byte[] getV() {
        return v;
    }

    public void setV(byte[] v) {
        this.v = v;
    }

    public byte[] getR() {
        return r;
    }

    public void setR(byte[] r) {
        this.r = r;
    }

    public byte[] getS() {
        return s;
    }

    public void setS(byte[] s) {
        this.s = s;
    }
}
