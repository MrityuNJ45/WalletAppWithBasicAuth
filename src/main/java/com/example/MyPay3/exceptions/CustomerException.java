package com.example.MyPay3.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomerException extends RuntimeException {

    public CustomerException() {
    }

    public CustomerException(String message) {
        super(message);
    }
}
