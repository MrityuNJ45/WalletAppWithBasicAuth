package com.example.MyPay3.service;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.models.Wallet;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private WalletRepo walletRepo;
    public Customer registerCustomer(Customer customer) {

        Wallet wallet = new Wallet();
        customer.setWallet(wallet);
        walletRepo.save(wallet);
        return customerRepo.save(customer);

    }

    public Customer addMoneyToCustomerWallet(String email, Integer money){

        Customer customer = customerRepo.findByEmail(email);
        Wallet wallet = customer.getWallet();
        wallet.setBalance(wallet.getBalance() + money);
        walletRepo.save(wallet);
        return customerRepo.save(customer);
    }

    public Customer withdrawMoneyFromCustomerWallet(String email, Integer money) throws IllegalStateException{

        Customer customer = customerRepo.findByEmail(email);
        Wallet wallet = customer.getWallet();
        if(wallet.getBalance() < money){
            throw new IllegalStateException("Insufficient amount to withdraw");
        }
        wallet.setBalance(wallet.getBalance() - money);
        walletRepo.save(wallet);
        return customerRepo.save(customer);

    }

}
