package com.example.MyPay3.models;

public enum Currency {
    USD(1.0),
    EUR( 0.83),
    INR( 73.03);

    private final double conversionRateToUSD;

    Currency( double conversionRateToUSD) {

        this.conversionRateToUSD = conversionRateToUSD;
    }
    public double convert(double amount, Currency toCurrency) {
        double convertedAmount = amount / this.conversionRateToUSD * toCurrency.conversionRateToUSD;
        return Math.round(convertedAmount * 100) / 100.0; // Round to 2 decimal places
    }
}
