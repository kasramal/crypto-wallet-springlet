package com.demohouse.walletcore.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorDTO {
    String message;
    Long code;
    String exception;
}
