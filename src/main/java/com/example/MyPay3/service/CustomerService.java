package com.example.MyPay3.service;

import com.example.MyPay3.models.Customer;
import com.example.MyPay3.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


    @Autowired
    private CustomerRepo customerRepo;
    public Customer registerCustomer(Customer customer) {

        return customerRepo.save(customer);

    }

    public Customer addMoneyToCustomerWallet(String email, Integer money){

        return customerRepo.findByEmail(email);

    }

    public Customer withdrawMoneyFromCustomerWallet(String email, Integer money){

        return customerRepo.findByEmail(email);

    }

}
