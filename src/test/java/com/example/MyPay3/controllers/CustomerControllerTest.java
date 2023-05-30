package com.example.MyPay3.controllers;

import com.example.MyPay3.config.SecurityConfig;
import com.example.MyPay3.models.Currency;
import com.example.MyPay3.models.Customer;
import com.example.MyPay3.models.MoneyDTO;
import com.example.MyPay3.models.Wallet;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.WalletRepo;
import com.example.MyPay3.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(SecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private CustomerRepo customerRepo;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Authentication authentication;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void expectsToSaveACustomer() throws Exception {
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                        .andExpect(status().isCreated());

    }

    @Test
    public void expectsToGiveBadRequestWhenInvalidCustomerIsPosted() throws Exception {

        Customer customer = null;

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void expectsToAddMoneyIfValidUserAndGiveStatusOk() throws Exception {

        MoneyDTO moneyToSend = new MoneyDTO(10.0, Currency.INR);
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
        Mockito.when(customerService.addMoneyToCustomerWallet(customer.getEmail(),moneyToSend)).thenReturn(customer);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moneyToSend))
                .with(user(customer.getName()))).andExpect(status().isOk());

    }

    @Test
    public void expectsToGiveStatus403IfInvalidUser() throws Exception {
        MoneyDTO moneyToSend = new MoneyDTO(10.0, Currency.INR);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(moneyToSend)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void expectsToWithDrawMoneyIfValidUser() throws Exception {
        MoneyDTO moneyToSend = new MoneyDTO(10.0, Currency.INR);
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
        Mockito.when(customerService.withdrawMoneyFromCustomerWallet(customer.getEmail(),moneyToSend)).thenReturn(customer);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer))
                .with(user(customer.getName()))).andExpect(status().isOk());

    }

    @Test
    public void expectsToGive403IfInvalidUserForWithdrawingMoney() throws Exception {
        MoneyDTO moneyToSend = new MoneyDTO(10.0, Currency.INR);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moneyToSend))).andExpect(status().isUnauthorized());
    }

    @Test
    public void expectsToGiveHttpStatus202WhenTryingToAddMoneyToOtherUserWallet() throws Exception {

        MoneyDTO moneyToSend = new MoneyDTO(10.0, Currency.INR);
        String otherUserEmail = "validReceiver@gmail.com";
        Customer customer = new Customer();
        this.mockMvc.perform(put("/customer/addmoney/{otherUserEmail}/{money}",otherUserEmail,money).with(user("valid@gmail.com")))
                .andExpect(status().isOk());

    }
//
//    @Test
//    public void expectsToGiveHttpStatus403WhenTryingToAddMoneyWithInvalidUser() throws Exception {
//        Double money = 100.0;
//        String otherUserEmail = "aaaa@gmail.com";
//
//        this.mockMvc.perform(put("/customer/addmoney/{otherUserEmail}/{money}", otherUserEmail, money))
//                .andExpect(status().isUnauthorized());
//    }







}