package com.example.MyPay3.models;

import com.example.MyPay3.exceptions.WalletException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {


    @Test
    public void expectsToCreateAWallet(){

        assertDoesNotThrow(() -> new Wallet());

    }

    @Test
    public void expectsToDepositMoneyInWallet() {
        Wallet wallet = Wallet.builder().currency(Currency.INR).balance(100.0).build();
        WalletDTO moneyDTO = new WalletDTO(1.0, Currency.USD);
        Wallet actual = wallet.deposit(moneyDTO);
        Wallet expected = Wallet.builder().currency(Currency.INR).balance(173.03).build();
        assertEquals(actual, expected);

    }

    @Test
    public void expectsToThrowExceptionWhenCurrencyIsNotAssociatedToWalletOfReceiverWhileDeposit() {
        Wallet wallet = Wallet.builder().balance(100.0).build();
        WalletDTO moneyDTO = new WalletDTO(1.0, Currency.USD);
        WalletException thrown = assertThrows(WalletException.class, () -> {
            wallet.deposit(moneyDTO);
        }) ;

        assertEquals(thrown.getMessage(), "receiver's wallet not associated with any currency");

    }

    @Test
    public void expectsToThrowExceptionWhenCurrencyIsNotAssociatedToWalletOfSenderWhileWithdraw() {
        Wallet wallet = Wallet.builder().balance(100.0).build();
        WalletDTO moneyDTO = new WalletDTO(1.0, Currency.USD);
        WalletException thrown = assertThrows(WalletException.class, () -> {
            wallet.withdraw(moneyDTO);
        }) ;

        assertEquals(thrown.getMessage(), "sender's wallet not associated with any currency");

    }

    @Test
    public void expectsToWithdrawMoneyFromWalletWhenSufficientBalanceIsThere() {
        Wallet wallet = Wallet.builder().currency(Currency.INR).balance(100.0).build();
        WalletDTO moneyDTO = new WalletDTO(1.0, Currency.USD);
        Wallet actual = wallet.withdraw(moneyDTO);
        Wallet expected = Wallet.builder().currency(Currency.INR).balance(100.0 - 73.03).build();
        assertEquals(actual, expected);
    }

    @Test
    public void expectsToShowExceptionWhileWithdrawingMoneyFromWalletWhenInSufficientBalanceIsThere() {
        Wallet wallet = Wallet.builder().currency(Currency.INR).balance(10.0).build();
        WalletDTO moneyDTO = new WalletDTO(1.0, Currency.USD);

        WalletException thrown = assertThrows(WalletException.class,() -> {
            wallet.withdraw(moneyDTO);
        });

        assertEquals(thrown.getMessage(), "Insufficient balance in sender's wallet");
    }


}