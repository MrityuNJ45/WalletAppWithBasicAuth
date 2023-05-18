package com.example.MyPay3.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.context.annotation.Scope;

@Entity
@Scope("prototype")
public class Wallet {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;


    private Double balance = 0.0;

    public Wallet(Integer walletId, Double balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public Wallet() {
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }




}
