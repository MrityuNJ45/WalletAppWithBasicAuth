package com.example.MyPay3.models;

import com.example.MyPay3.exceptions.WalletException;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Scope("prototype")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {

    private final Double INITIAL_AMOUNT = 0.0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

    private Integer userId;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Double balance = INITIAL_AMOUNT;

    public Wallet(Integer walletId) {
        this.walletId = walletId;
    }

    public Wallet deposit(MoneyDTO moneyDTO) {

        Double convertedAmount = moneyDTO.getCurrency().convert(moneyDTO.getAmount(), this.currency);
        Double resultAmount = this.balance + convertedAmount;
        return new Wallet(this.walletId, this.userId,this.currency ,resultAmount);

    }

    public Wallet withdraw(MoneyDTO moneyDTO) {
        Double convertedAmount = moneyDTO.getCurrency().convert(moneyDTO.getAmount(), this.currency);
        System.out.println(convertedAmount);
        if(this.balance < convertedAmount) {
            throw new WalletException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }
        Double resultAmount = this.balance - convertedAmount;
        return new Wallet(this.walletId, this.userId,this.currency ,resultAmount);
    }

}
