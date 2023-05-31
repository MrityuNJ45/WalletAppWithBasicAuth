package com.example.MyPay3.controllers;

import com.example.MyPay3.models.*;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {


    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/customers")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer response =  customerService.registerCustomer(customer);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/customer/add")
    public ResponseEntity<Customer> addMoneyToCustomerWallet(@AuthenticationPrincipal(expression = "username") String email, @RequestBody WalletDTO moneyDTO){

        Customer response = customerService.addMoneyToCustomerWallet(email,moneyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/customer/withdraw")
    public ResponseEntity<Customer> withDrawMoneyFromCustomerHandler(@AuthenticationPrincipal(expression = "username") String email, @RequestBody WalletDTO moneyDTO){
        Customer response = customerService.withdrawMoneyFromCustomerWallet(email,moneyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/customer/addmoney/{otherUserEmail}")
    public ResponseEntity<MoneyTransferResponse> addMoneyToOtherUserWalletHandler(@AuthenticationPrincipal(expression = "username") String email, @PathVariable("otherUserEmail") String otherUserEmail, @RequestBody WalletDTO moneyDTO) {
        MoneyTransferResponse response = customerService.addMoneyToOtherUserWallet(email, otherUserEmail, moneyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/customer")
    public ResponseEntity<Customer> getCustomer(@AuthenticationPrincipal(expression = "username") String email){
        Customer response = customerRepo.findByEmail(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/transactions/get/all")
    public ResponseEntity<List<Transaction>> getAllTransactionsOfUser(@AuthenticationPrincipal(expression = "username") String email) {
        List<Transaction> transactions = customerService.getTransactionHistoryByUserEmail(email);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

//    @PostMapping("/add/wallet/user")
//    public ResponseEntity<Wallet> addWalletToUser(@AuthenticationPrincipal(expression = "username") String email, @RequestBody WalletDTO walletDTO) {
//        //Wallet  wallet  = customerService.addWalletToUser(email, walletDTO);
//        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
//    }


}
