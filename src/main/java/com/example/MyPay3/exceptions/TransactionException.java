package com.example.MyPay3.exceptions;


import org.springframework.http.HttpStatus;

public class TransactionException extends RuntimeException{

    private HttpStatus httpStatus;

    public TransactionException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
