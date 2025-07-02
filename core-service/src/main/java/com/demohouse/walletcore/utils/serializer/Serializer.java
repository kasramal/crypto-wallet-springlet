package com.demohouse.walletcore.utils.serializer;

import java.util.List;

public abstract class Serializer {

    private List<Content> contents;

    public Serializer append() {
        return this;
    }
}
