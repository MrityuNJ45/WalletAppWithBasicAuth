package com.example.MyPay3.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.context.annotation.Scope;

@Entity
@Scope("prototype")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;
    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Customer(Integer customerId, String name, String email, Currency currency, String password, Wallet wallet) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.currency = currency;
        this.password = password;
        this.wallet = wallet;
    }

    public Customer(){

    }

    public Customer(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Customer(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
