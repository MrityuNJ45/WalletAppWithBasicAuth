package com.example.MyPay3.service;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.repository.CustomerRepo;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void expectsToSaveCustomer(){

        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Mockito.when(customerRepo.save(customer)).thenReturn(customer);

        assertEquals(customerService.registerCustomer(customer), customer);

    }

    @Test
    public void expectsToAddMoneyToCustomerWallet(){
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
        assertEquals(customerService.addMoneyToCustomerWallet(customer.getEmail(),100),customer);
    }

    @Test
    public void expectsToWithDrawMoneyFromCustomerWallet(){
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
        assertEquals(customerService.withdrawMoneyFromCustomerWallet(customer.getEmail(),100),customer);
    }

}