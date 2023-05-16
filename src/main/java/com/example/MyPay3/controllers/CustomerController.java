package com.example.MyPay3.controllers;

import com.example.MyPay3.models.Customer;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {


    @PostMapping("/customers")
    public String addCustomer(@RequestBody Customer customer){
        return "Customer is added";
    }

    @PutMapping("/customer/add/{money}")
    public String addMoneyToCustomerWallet(@AuthenticationPrincipal(expression = "username") String username, @PathVariable("money") Integer money){
        return "money added to : " + username;
    }





}
