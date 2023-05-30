package com.example.MyPay3.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerException extends RuntimeException {

    private HttpStatus httpStatus;

    public CustomerException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
