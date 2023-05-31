package com.example.MyPay3.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetails {

    private LocalDateTime timeStamp;

    private String message ;

    private String description;



    public ErrorDetails() {
        super();
    }



}
