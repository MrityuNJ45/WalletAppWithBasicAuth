package com.example.MyPay3.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {


    private Double amount;

    private Currency currency;

    public Wallet toWallet() {
        Wallet wallet = Wallet.builder().balance(amount).currency(currency).build();
        return wallet;
    }

}
