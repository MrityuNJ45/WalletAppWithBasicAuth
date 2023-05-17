package com.example.MyPay3.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {


    @Test
    public void expectsToCreateAWallet(){

        assertDoesNotThrow(() -> new Wallet());

    }

}