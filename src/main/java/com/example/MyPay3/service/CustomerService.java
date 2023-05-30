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
        Customer savingCustomer = Customer.builder().customerId(customer.getCustomerId()).wallet(wallet).name(customer.getName())
                .password(customer.getPassword()).email(customer.getEmail()).currency(customer.getCurrency()).build();
        customer.setWallet(wallet);
        walletRepo.save(wallet);
        return customerRepo.save(savingCustomer);

    }

    public Customer addMoneyToCustomerWallet(String email, Double money){

        Customer customer = customerRepo.findByEmail(email);
        Wallet wallet = customer.getWallet();
        wallet.setBalance(wallet.getBalance() + money);
        walletRepo.save(wallet);
        return customerRepo.save(customer);
    }

    public Customer withdrawMoneyFromCustomerWallet(String email,Double money) throws IllegalStateException{

        Customer customer = customerRepo.findByEmail(email);
        Wallet wallet = customer.getWallet();
        if(wallet.getBalance() < money){
            throw new IllegalStateException("Insufficient amount to withdraw");
        }
        wallet.setBalance(wallet.getBalance() - money);
        walletRepo.save(wallet);
        return customerRepo.save(customer);

    }

    public String addMoneyToOtherUserWallet(String senderEmail, String receiverEmail,Double money) throws IllegalStateException{
        Customer recievingCustomer = customerRepo.findByEmail(receiverEmail);
        if(recievingCustomer == null){
            throw new IllegalStateException("Invalid receiver's email address");
        }

        Customer sendingCustomer = customerRepo.findByEmail(senderEmail);
        Wallet senderWallet = sendingCustomer.getWallet();
        Wallet receiverWallet = recievingCustomer.getWallet();
        if(senderWallet.getBalance() < money){
            throw new IllegalStateException("Insufficient amount to transfer");
        }

        double amountInRecieverCurrency = sendingCustomer.getCurrency().convert(money,recievingCustomer.getCurrency());
        receiverWallet.setBalance(receiverWallet.getBalance() + amountInRecieverCurrency);
        senderWallet.setBalance(senderWallet.getBalance() - money);
        walletRepo.save(receiverWallet);
        walletRepo.save(senderWallet);
        customerRepo.save(sendingCustomer);
        customerRepo.save(recievingCustomer);
        return "Money transfer successfull";

    }

}
