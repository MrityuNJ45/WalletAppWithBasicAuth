package com.example.MyPay3.service;

import com.example.MyPay3.exceptions.CustomerException;
import com.example.MyPay3.exceptions.TransactionException;
import com.example.MyPay3.models.*;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.TransactionRepo;
import com.example.MyPay3.repository.WalletRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.money.MonetaryAmountFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {


    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Transactional
    public Customer registerCustomer(Customer customer) {

        Wallet wallet = new Wallet();
        Customer savingCustomer = Customer.builder().customerId(customer.getCustomerId()).wallet(wallet).name(customer.getName())
                .password(customer.getPassword()).email(customer.getEmail()).build();

        walletRepo.save(wallet);
        return customerRepo.save(savingCustomer);

    }

    @Transactional
    public Customer addMoneyToCustomerWallet(String email, MoneyDTO moneyDTO){

        Customer customer = customerRepo.findByEmail(email);
        Wallet wallet = customer.getWallet();
        Wallet updatedWallet = wallet.deposit(moneyDTO);
        Transaction transaction = Transaction.builder().senderId(customer.getCustomerId())
                .receiverId(customer.getCustomerId()).amount(moneyDTO.getAmount())
                .build();
        transactionRepo.save(transaction);
        walletRepo.save(updatedWallet);
        return customerRepo.save(customer);

    }

    public Customer withdrawMoneyFromCustomerWallet(String email, MoneyDTO moneyDTO) throws IllegalStateException{

        Customer customer = customerRepo.findByEmail(email);
        Wallet wallet = customer.getWallet();
        Wallet updatedWallet = wallet.withdraw(moneyDTO);
        Transaction transaction = Transaction.builder().senderId(customer.getCustomerId())
                .receiverId(customer.getCustomerId()).amount(moneyDTO.getAmount())
                .build();
        transactionRepo.save(transaction);
        walletRepo.save(updatedWallet);
        return customerRepo.save(customer);

    }

    @Transactional
    public MoneyTransfer addMoneyToOtherUserWallet(String senderEmail, String receiverEmail, MoneyDTO moneyDTO){
        Customer recievingCustomer = customerRepo.findByEmail(receiverEmail);

        if(recievingCustomer == null) {
            throw new CustomerException(HttpStatus.NOT_FOUND, "Invalid receiving customer email");
        }

        Customer sendingCustomer = customerRepo.findByEmail(senderEmail);
        Wallet senderWallet = sendingCustomer.getWallet();
        Wallet receiverWallet = recievingCustomer.getWallet();
        senderWallet = senderWallet.withdraw(moneyDTO);
        receiverWallet = receiverWallet.deposit(moneyDTO);

        walletRepo.save(senderWallet);
        walletRepo.save(receiverWallet);
        customerRepo.save(sendingCustomer);
        customerRepo.save(recievingCustomer);
        Transaction transaction = Transaction.builder().senderId(sendingCustomer.getCustomerId())
                .receiverId(recievingCustomer.getCustomerId()).amount(moneyDTO.getAmount())
                .build();
        transactionRepo.save(transaction);
        return new MoneyTransfer(true);

    }

    public List<Transaction> getTransactionHistoryByUserId(Integer userId) {
        List<Transaction> transactions = transactionRepo.findBySenderIdOrReceiverId(userId, userId);
        if(transactions.size() == 0) {
            throw new TransactionException(HttpStatus.NOT_FOUND, "No transactions found");
        }
        return transactions;
    }


}
