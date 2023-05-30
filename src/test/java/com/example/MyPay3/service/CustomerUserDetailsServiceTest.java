package com.example.MyPay3.service;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.repository.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CustomerUserDetailsServiceTest {


    @InjectMocks
    private CustomerUserDetailsService customerUserDetailsService;

    @Mock
    private CustomerRepo customerRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void expectsToGiveUserDetailsObject(){

        Customer customer = new Customer("morty", "m@gmail.com","1234" );
        Mockito.when(customerRepo.findByEmail(customer.getEmail())).thenReturn(customer);
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(customer.getEmail());
        assertEquals(userDetails.getUsername(), customer.getEmail());

    }

    @Test
    public void expectsToGiveException(){
        String invalidEmail = "invalid@gmail.com";
        Mockito.when(customerRepo.findByEmail(invalidEmail)).thenReturn(null);
        BadCredentialsException thrown = assertThrows(BadCredentialsException.class,() -> {customerUserDetailsService.loadUserByUsername(invalidEmail);});
        assertEquals("User Details not found with this username: " + invalidEmail, thrown.getMessage());
    }

}