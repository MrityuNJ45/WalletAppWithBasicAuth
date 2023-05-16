package com.example.MyPay3.controllers;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.repository.CustomerRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepo customerRepo;

    @PostMapping("/customers")
    public String addCustomer(@RequestBody Customer customer){
        return "Customer is added";
    }

    @PutMapping("/customer/add/{money}")
    public String addMoneyToCustomerWallet(@AuthenticationPrincipal(expression = "username") String email, @PathVariable("money") Integer money){
        Customer customer = customerRepo.findByEmail(email);
        if(customer == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return "money added to : " + email;
    }

    @PutMapping("/customer/withdraw/{money}")
    public String withDrawMoneyFromCustomerHandler(@AuthenticationPrincipal(expression = "username") String username, @PathVariable("money") Integer money){
        return "money withdrawn from : " + username;
    }





}
