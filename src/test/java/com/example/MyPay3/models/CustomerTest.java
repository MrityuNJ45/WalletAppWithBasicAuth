package com.example.MyPay3.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    public void expectsToCreateCustomer(){
        assertDoesNotThrow(() -> new Customer("mohit","m@gmail.com","1234"));
    }


}