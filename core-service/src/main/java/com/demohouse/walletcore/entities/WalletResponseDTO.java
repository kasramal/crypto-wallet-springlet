package com.demohouse.walletcore.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WalletResponseDTO<T> {
    T data;
    String message;
    Date timestamp;
    Boolean success;
}
