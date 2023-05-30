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

@Entity
@Scope("prototype")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Double balance = 0.0;

    public Wallet deposit(MoneyDTO moneyDTO) {

        Double convertedAmount = moneyDTO.getCurrency().convert(moneyDTO.getAmount(), this.currency);
        Double resultAmount = this.balance + convertedAmount;
        return new Wallet(this.walletId, this.currency ,resultAmount);

    }

    public Wallet withdraw(MoneyDTO moneyDTO) {
        Double convertedAmount = moneyDTO.getCurrency().convert(moneyDTO.getAmount(), this.currency);
        System.out.println(convertedAmount);
        if(this.balance < convertedAmount) {
            throw new WalletException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }
        Double resultAmount = this.balance - convertedAmount;
        return new Wallet(this.walletId, this.currency, resultAmount);
    }

}
