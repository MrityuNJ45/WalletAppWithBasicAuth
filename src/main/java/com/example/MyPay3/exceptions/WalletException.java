package com.example.MyPay3.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class WalletException extends RuntimeException {
    public WalletException() {
    }

    public WalletException(String message) {
        super(message);
    }
}
