package com.example.MyPay3.service;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.models.Wallet;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.WalletRepo;
import jakarta.annotation.security.RunAs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private WalletRepo walletRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void expectsToSaveCustomer(){

        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Wallet wallet = new Wallet();
        Customer savedCustomer = new Customer(customer.getName(), customer.getEmail(), customer.getPassword());
        savedCustomer.setWallet(wallet);
        Mockito.when(customerRepo.save(customer)).thenReturn(savedCustomer);
        assertEquals(customerService.registerCustomer(customer), savedCustomer);

    }

    @Test
    public void expectsToAddMoneyToCustomerWallet(){
        Integer money = 100;
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        customer.setWallet(new Wallet());
        Customer updatedWalletCustomer = new Customer(customer.getName(), customer.getEmail(), customer.getPassword());
        Wallet updatedWallet = customer.getWallet();
        updatedWallet.setBalance(updatedWallet.getBalance() + money);
        updatedWalletCustomer.setWallet(updatedWallet);
        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
        Mockito.when(customerRepo.save(customer)).thenReturn(updatedWalletCustomer);
        assertEquals(customerService.addMoneyToCustomerWallet(customer.getEmail(),money),updatedWalletCustomer);
    }

    @Test
    public void expectsToWithDrawMoneyFromCustomerWallet(){
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
        assertEquals(customerService.withdrawMoneyFromCustomerWallet(customer.getEmail(),100),customer);
    }

}