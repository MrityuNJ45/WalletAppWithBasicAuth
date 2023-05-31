package com.example.MyPay3.controllers;

import com.example.MyPay3.config.SecurityConfig;
import com.example.MyPay3.exceptions.WalletException;
import com.example.MyPay3.models.*;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

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

        WalletDTO moneyToSend = new WalletDTO(10.0, Currency.INR);
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
        WalletDTO moneyToSend = new WalletDTO(10.0, Currency.INR);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(moneyToSend)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void expectsToWithDrawMoneyIfValidUser() throws Exception {
        WalletDTO moneyToSend = new WalletDTO(10.0, Currency.INR);
        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
        Mockito.when(customerService.withdrawMoneyFromCustomerWallet(customer.getEmail(),moneyToSend)).thenReturn(customer);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer))
                .with(user(customer.getName()))).andExpect(status().isOk());

    }

    @Test
    public void expectsToGive403IfInvalidUserForWithdrawingMoney() throws Exception {
        WalletDTO moneyToSend = new WalletDTO(10.0, Currency.INR);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moneyToSend))).andExpect(status().isUnauthorized());
    }

    @Test
    public void expectsToGiveHttpStatus202WhenTryingToAddMoneyToOtherUserWallet() throws Exception {

        WalletDTO moneyToSend = new WalletDTO(10.0, Currency.INR);
        String otherUserEmail = "validReceiver@gmail.com";
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(put("/customer/addmoney/{otherUserEmail}",otherUserEmail).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moneyToSend)).with(user("valid@gmail.com")))
                .andExpect(status().isOk());

    }

    @Test
    public void expectsToGiveHttpStatus403WhenTryingToAddMoneyWithInvalidUser() throws Exception {
        WalletDTO moneyToSend = new WalletDTO(10.0, Currency.INR);
        String otherUserEmail = "aaaa@gmail.com";
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(put("/customer/addmoney/{otherUserEmail}", otherUserEmail).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(moneyToSend)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAllTransactionsOfUser_Success() throws Exception {

        String authenticatedEmail = "test@example.com";

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        Mockito.when(customerService.getTransactionHistoryByUserEmail(authenticatedEmail)).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/get/all")
                        .with(user("test@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(status().isOk());

        Mockito.verify(customerService, Mockito.times(1)).getTransactionHistoryByUserEmail(authenticatedEmail);
    }


    @Test
    public void testAddWalletToUser_Success() throws Exception {
        String email = "test@example.com";
        Wallet wallet = new Wallet();
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/add/wallet/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wallet))
                        .with(user(email)))
                .andExpect(status().isCreated());

    }


    @Test
    public void expectsToThrowExceptionWhenTryingToAddWalletToUserWhoAlreadyHasAWallet() throws Exception {
        String email = "test@example.com";
        Wallet wallet = new Wallet();
        doThrow(new WalletException(HttpStatus.BAD_REQUEST,"Wallet Already activated / added")).when(customerService).addWalletToUser(email, wallet);
        Mockito.when(customerService.addWalletToUser(email,wallet)).thenThrow(new WalletException(HttpStatus.BAD_REQUEST,"Wallet Already activated / added"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/add/wallet/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wallet))
                        .with(user(email)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Wallet Already activated / added"));
        Mockito.verify(customerService, times(1)).addWalletToUser(email, wallet);

    }



}