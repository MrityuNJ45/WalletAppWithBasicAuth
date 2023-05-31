package com.example.MyPay3.controllers;

import com.example.MyPay3.models.Transaction;
import com.example.MyPay3.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

//    @Autowired
//    private CustomerService customerService;
//    @GetMapping("/get/all")
//    public ResponseEntity<List<Transaction>> getAllTransactionsOfUser(@AuthenticationPrincipal(expression = "username") String email) {
//        List<Transaction> transactions = customerService.getTransactionHistoryByUserEmail(email);
//        return new ResponseEntity<>(transactions, HttpStatus.OK);
//    }

}
