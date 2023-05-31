package com.example.MyPay3.service;

import com.example.MyPay3.exceptions.CustomerException;
import com.example.MyPay3.exceptions.TransactionException;
import com.example.MyPay3.exceptions.WalletException;
import com.example.MyPay3.models.*;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.TransactionRepo;
import com.example.MyPay3.repository.WalletRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

        return customerRepo.save(customer);

    }

    @Transactional
    public Customer addMoneyToCustomerWallet(String email, WalletDTO walletDTO){

        Customer customer = customerRepo.findByEmail(email);
        if(customer.isWalletAdded() == false) {
            throw new CustomerException("Wallet not added");
        }
        Wallet wallet = customer.getWallet();
        Wallet updatedWallet = wallet.deposit(walletDTO);
        Transaction transaction = Transaction.builder().senderId(customer.getCustomerId())
                .receiverId(customer.getCustomerId()).amount(walletDTO.getAmount())
                .build();
        transactionRepo.save(transaction);
        walletRepo.save(updatedWallet);
        return customerRepo.save(customer);

    }

    @Transactional
    public Customer withdrawMoneyFromCustomerWallet(String email, WalletDTO walletDTO){
        Customer customer = customerRepo.findByEmail(email);
        if(customer.isWalletAdded() == false) {
            throw new CustomerException("Wallet not added");
        }
        Wallet wallet = customer.getWallet();
        Wallet updatedWallet = wallet.withdraw(walletDTO);
        Transaction transaction = Transaction.builder().senderId(customer.getCustomerId())
                .receiverId(customer.getCustomerId()).amount(walletDTO.getAmount())
                .build();
        transactionRepo.save(transaction);
        walletRepo.save(updatedWallet);
        return customerRepo.save(customer);
    }

    @Transactional
    public MoneyTransferResponse addMoneyToOtherUserWallet(String senderEmail, String receiverEmail, WalletDTO moneyDTO){
        Customer recievingCustomer = customerRepo.findByEmail(receiverEmail);
        if(recievingCustomer == null) {
            throw new CustomerException("Invalid receiving customer email");
        }
        if(recievingCustomer.isWalletAdded() == false) {
            throw new CustomerException("Wallet not added to receiving customer's account");
        }
        Customer sendingCustomer = customerRepo.findByEmail(senderEmail);
        if(sendingCustomer.isWalletAdded() == false) {
            throw new CustomerException("Wallet not added to sending customer's account");
        }
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
        return new MoneyTransferResponse(true);

    }

    public List<Transaction> getTransactionHistoryByUserEmail(String email) {
        Customer customer = customerRepo.findByEmail(email);
        Integer customerId = customer.getCustomerId();
        List<Transaction> transactions = transactionRepo.findBySenderIdOrReceiverId(customerId, customerId);
        if(transactions.size() == 0) {
            throw new TransactionException("No transactions found");
        }
        return transactions;
    }

    public Wallet addWalletToUser(String email, Wallet wallet) {
        Customer customer = customerRepo.findByEmail(email);
        if(customer.isWalletAdded()) {
            throw new WalletException("Wallet Already activated / added");
        }
        Customer walletAddedCustomer = new Customer(customer.getCustomerId(), customer.getName(), customer.getEmail(), customer.getPassword(), wallet);
        customerRepo.save(walletAddedCustomer);
        return walletRepo.save(wallet);
    }


}
