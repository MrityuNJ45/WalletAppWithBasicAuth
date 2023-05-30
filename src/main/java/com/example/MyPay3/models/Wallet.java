package com.example.MyPay3.models;

import com.example.MyPay3.exceptions.WalletException;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;

@Entity
@Scope("prototype")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

    private Double balance = 0.0;

    public Wallet deposit(MoneyDTO moneyDTO) {

        Double resultAmount = this.balance + moneyDTO.getAmount();
        return new Wallet(this.walletId, resultAmount);

    }

    public Wallet withdraw(MoneyDTO moneyDTO) {
        if(this.balance < moneyDTO.getAmount()) {
            throw new WalletException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }
        Double resultAmount = this.balance - moneyDTO.getAmount();
        return new Wallet(this.walletId, resultAmount);
    }

}
