package com.example.MyPay3.exceptions;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class TransactionException extends RuntimeException{


    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);
    }
}
