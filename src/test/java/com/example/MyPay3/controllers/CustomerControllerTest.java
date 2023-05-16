package com.example.MyPay3.controllers;

import com.example.MyPay3.config.SecurityConfig;
import com.example.MyPay3.models.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

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

                .andExpect(status().isOk()).andExpect(content().string("Customer is added"));
    }

    @Test
    public void expectsToAddMoneyIfValidUser() throws Exception {
        Integer money = 100;
        this.mockMvc.perform(put("/customer/add/{money}", money).with(user("morty"))).andExpect(status().isOk())
                .andExpect(content().string("money added to : morty"));
    }

    @Test
    public void expectsToGive403IfInvalidUser() throws Exception {
        Integer money = 100;
        this.mockMvc.perform(put("/customer/add/{money}", money)).andExpect(status().isUnauthorized());
    }

    @Test
    public void expectsToWithDrawMoneyIfValidUser() throws Exception {
        Integer money = 100;
        this.mockMvc.perform(put("/customer/withdraw/{money}", money).with(user("morty"))).andExpect(status().isOk())
                .andExpect(content().string("money withdrawn from : morty"));
    }

    @Test
    public void expectsToGive403IfInvalidUserForWithdrawingMoney() throws Exception {
        Integer money = 100;
        this.mockMvc.perform(put("/customer/withdraw/{money}", money)).andExpect(status().isUnauthorized());
    }


}