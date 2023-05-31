package com.example.MyPay3.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorDetails> myExpHandler(TransactionException pe, WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimeStamp(LocalDateTime.now());
        err.setMessage(pe.getMessage());
        err.setDescription(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<ErrorDetails> customerExceptionHandler(CustomerException ce, WebRequest request) {
        ErrorDetails err = new ErrorDetails();
        err.setTimeStamp(LocalDateTime.now());
        if(ce.getMessage() == "receiver's wallet not associated with any currency" || ce.getMessage() == "Wallet not added to sending customer's account"){
            err.setMessage(ce.getMessage());
            err.setDescription(request.getDescription(false));
            return new ResponseEntity<ErrorDetails>(err, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);


    }
    @ExceptionHandler(WalletException.class)
    public ResponseEntity<ErrorDetails> customerExceptionHandler(WalletException ce, WebRequest request) {
        ErrorDetails err = new ErrorDetails();
        err.setTimeStamp(LocalDateTime.now());
        err.setMessage(ce.getMessage());
        err.setDescription(request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> myAnyExpHandler(Exception ie,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimeStamp(LocalDateTime.now());
        err.setMessage(ie.getMessage());
        err.setDescription(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

}
