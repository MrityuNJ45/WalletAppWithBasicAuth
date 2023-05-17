package com.example.MyPay3.repository;

import com.example.MyPay3.models.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WalletRepoTest {

    @Autowired
    private WalletRepo walletRepo;

    @Test
    public void expectsToSaveWalletToDatabase(){
        Wallet wallet = new Wallet();
        walletRepo.save(wallet);
        assertTrue(wallet.getWalletId() > 0);
    }

}