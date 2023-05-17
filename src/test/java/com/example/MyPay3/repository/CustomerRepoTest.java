package com.example.MyPay3.repository;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.models.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepoTest {

    @Autowired
    private CustomerRepo customerRepo;

    @Test
    public void expectsToSaveACustomer(){
        Customer customer = new Customer();
        customer.setName("mansi");
        customer.setPassword("1234");
        customer.setEmail("m@gmail.com");
        customer.setWallet(new Wallet());
        customerRepo.save(customer);
        assertTrue(customer.getCustomerId() > 0);
    }

    @Test
    public void expectsToGetUserWithEmail(){
        Customer customer = new Customer();
        customer.setName("mansi");
        customer.setPassword("1234");
        customer.setEmail("mansi@gmail.com");
        customer.setWallet(new Wallet());
        customerRepo.save(customer);
        Customer customerWithEmail = customerRepo.findByEmail("mansi@gmail.com");
        assertEquals(customer.getEmail(),customerWithEmail.getEmail());

    }

}