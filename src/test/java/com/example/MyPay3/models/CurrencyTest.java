package com.example.MyPay3.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    public void expectsToGiveConvertedValue73Point03WhenConverting1DollarToRupees(){

        Currency dollar = Currency.USD;
        Currency rupee = Currency.INR;
        double convertedValue = dollar.convert(1,rupee);
        assertEquals(73.03, convertedValue);
    }

    @Test
    public void expectsToGiveConvertedValue1WhenConverting73Point03RupeesToDollar(){

        Currency dollar = Currency.USD;
        Currency rupees = Currency.INR;
        double convertedValue = rupees.convert(73.03,dollar);
        assertEquals(1.0, convertedValue);
    }

}