package com.example.MyPay3.exceptions;

import org.springframework.http.HttpStatus;

public class WalletException extends RuntimeException {
    private HttpStatus httpStatus;

    public WalletException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
