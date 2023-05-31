package com.example.MyPay3.models;

import com.example.MyPay3.exceptions.WalletException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;

import java.util.List;

@Entity
@Scope("prototype")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {

    @JsonIgnore
    private final Double INITIAL_AMOUNT = 0.0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

    @Enumerated(EnumType.STRING)
    private Currency currency;


    private Double balance = INITIAL_AMOUNT;

    public Wallet(Integer walletId) {
        this.walletId = walletId;
    }


    public Wallet deposit(WalletDTO walletDTO ) {
        if(this.currency == null) {
            throw new WalletException("receiver's wallet not associated with any currency");
        }
        Double convertedAmount = walletDTO.getCurrency().convert(walletDTO.getAmount(), this.currency);
        Double resultAmount = this.balance + convertedAmount;
        return new Wallet(this.walletId,this.currency ,resultAmount);
    }


    public Wallet withdraw(WalletDTO moneyDTO) {
        if(this.currency == null) {
            throw new WalletException("sender's wallet not associated with any currency");
        }
        Double convertedAmount = moneyDTO.getCurrency().convert(moneyDTO.getAmount(), this.currency);

        if(this.balance < convertedAmount) {
            throw new WalletException("Insufficient balance in sender's wallet");
        }
        Double resultAmount = this.balance - convertedAmount;
        return new Wallet(this.walletId,this.currency ,resultAmount);
    }

}
