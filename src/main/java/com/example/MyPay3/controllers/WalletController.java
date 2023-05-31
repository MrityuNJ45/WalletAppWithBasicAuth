package com.example.MyPay3.controllers;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.models.WalletDTO;
import com.example.MyPay3.models.MoneyTransferResponse;
import com.example.MyPay3.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/")

    @PutMapping("/add/money")
    public ResponseEntity<Customer> addMoneyToCustomerWallet(@AuthenticationPrincipal(expression = "username") String email, @RequestBody WalletDTO moneyDTO){

        Customer response = customerService.addMoneyToCustomerWallet(email,moneyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Customer> withDrawMoneyFromCustomerHandler(@AuthenticationPrincipal(expression = "username") String email, @RequestBody WalletDTO moneyDTO){
        Customer response = customerService.withdrawMoneyFromCustomerWallet(email,moneyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/send/{otherUserEmail}")
    public ResponseEntity<MoneyTransferResponse> addMoneyToOtherUserWalletHandler(@AuthenticationPrincipal(expression = "username") String email, @PathVariable("otherUserEmail") String otherUserEmail, @RequestBody WalletDTO moneyDTO) {
        MoneyTransferResponse response = customerService.addMoneyToOtherUserWallet(email, otherUserEmail, moneyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
