package com.example.MyPay3.service;

import com.example.MyPay3.models.Currency;
import com.example.MyPay3.models.Customer;
import com.example.MyPay3.models.Wallet;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.WalletRepo;
import jakarta.annotation.security.RunAs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
class CustomerServiceTest {

//    @MockBean
//    private CustomerRepo customerRepo;
//
//    @MockBean
//    private WalletRepo walletRepo;
//
//   @InjectMocks
//    private CustomerService customerService;
//
//
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void expectsToSaveCustomer(){
//
//        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
//        Wallet wallet = new Wallet();
//        Customer savedCustomer = new Customer(customer.getName(), customer.getEmail(), customer.getPassword());
//        savedCustomer.setWallet(wallet);
//        Mockito.when(customerRepo.save(customer)).thenReturn(savedCustomer);
//        assertEquals(customerService.registerCustomer(customer), savedCustomer);
//
//    }
//
//    @Test
//    public void expectsToAddMoneyToCustomerWallet(){
//        Double money = 100.0;
//        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
//        customer.setWallet(new Wallet());
//        Customer updatedWalletCustomer = new Customer(customer.getName(), customer.getEmail(), customer.getPassword());
//        Wallet updatedWallet = customer.getWallet();
//        updatedWallet.setBalance(updatedWallet.getBalance() + money);
//        updatedWalletCustomer.setWallet(updatedWallet);
//        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
//        Mockito.when(customerRepo.save(customer)).thenReturn(updatedWalletCustomer);
//        assertEquals(customerService.addMoneyToCustomerWallet(customer.getEmail(),money),updatedWalletCustomer);
//    }
//
//    @Test
//    public void expectsToWithDrawMoneyFromCustomerWallet(){
//        Double money = 100.0;
//        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
//        Wallet wallet = new Wallet();
//        wallet.setBalance(200.0);
//        customer.setWallet(wallet);
//        Customer updatedWalletCustomer = new Customer(customer.getName(), customer.getEmail(), customer.getPassword());
//        Wallet updatedWallet = customer.getWallet();
//        updatedWallet.setBalance(updatedWallet.getBalance() - money);
//        updatedWalletCustomer.setWallet(updatedWallet);
//        Mockito.when(customerRepo.findByEmail(any(String.class))).thenReturn(customer);
//        Mockito.when(customerRepo.save(customer)).thenReturn(updatedWalletCustomer);
//        assertEquals(customerService.addMoneyToCustomerWallet(customer.getEmail(),money),updatedWalletCustomer);
//    }
//
//    @Test
//    public void expectsToThrowExceptionWhenAddingMoneyToInvalidReciever(){
//
//        Double money = 100.0;
//        Customer customer = new Customer("mohit", "m@gmail.com", "1234");
//        Wallet wallet = new Wallet();
//        wallet.setBalance(200.0);
//        customer.setWallet(wallet);
//        String invalidEmail = "invalid@gmail.com";
//        Mockito.when(customerRepo.findByEmail(invalidEmail)).thenReturn(null);
//        IllegalStateException thrown = assertThrows(IllegalStateException.class,() -> {
//            customerService.addMoneyToOtherUserWallet(customer.getEmail(), invalidEmail,money);
//        });
//        assertEquals("Invalid receiver's email address", thrown.getMessage());
//    }
//
//    @Test
//    public void expectsToThrowExceptionWhenAddingMoneyToCustomerWithInsufficientAmountInCustomerWallet(){
//        Double money = 500.0;
//        Customer sender = new Customer("mohit", "m@gmail.com", "1234");
//        Wallet wallet = new Wallet();
//        wallet.setBalance(200.0);
//        sender.setWallet(wallet);
//        Customer receiver = new Customer("mohit2", "m2@gmail.com", "1234");
//        Wallet receiverWallet = new Wallet();
//        receiverWallet.setBalance(200.0);
//        receiver.setWallet(receiverWallet);
//        Mockito.when(customerRepo.findByEmail(sender.getEmail())).thenReturn(sender);
//        Mockito.when(customerRepo.findByEmail(receiver.getEmail())).thenReturn(receiver);
//        IllegalStateException thrown = assertThrows(IllegalStateException.class,() -> {
//            customerService.addMoneyToOtherUserWallet(sender.getEmail(), receiver.getEmail(),money);
//        });
//        assertEquals("Insufficient amount to transfer", thrown.getMessage());
//
//    }
//
//    @Test
//    public void expectsToAddMoneyToReceiverAccount(){
//        Double money = 500.0;
//        Customer sender = new Customer("mohit", "m@gmail.com", "1234");
//        Wallet wallet = new Wallet();
//        sender.setCurrency(Currency.INR);
//        wallet.setBalance(1000.0);
//        sender.setWallet(wallet);
//        Customer receiver = new Customer("mohit2", "m2@gmail.com", "1234");
//        receiver.setCurrency(Currency.INR);
//        Wallet receiverWallet = new Wallet();
//        receiverWallet.setBalance(200.0);
//        receiver.setWallet(receiverWallet);
//        Mockito.when(customerRepo.findByEmail(sender.getEmail())).thenReturn(sender);
//        Mockito.when(customerRepo.findByEmail(receiver.getEmail())).thenReturn(receiver);
//        assertEquals("Money transfer successfull", customerService.addMoneyToOtherUserWallet(sender.getEmail(),receiver.getEmail(), money));
//        assertEquals(200 + money, receiver.getWallet().getBalance());
//    }
//
//
//    @Test
//    public void expectsToAddMoneyToReceiverWhenSenderAndReceiverCurrencyAreDifferent(){
//        Double oneDollar = 1.0;
//        Customer sender = new Customer("mohit", "m@gmail.com", "1234");
//        Wallet wallet = new Wallet();
//        sender.setCurrency(Currency.USD);
//        wallet.setBalance(10.0);
//        sender.setWallet(wallet);
//        Customer receiver = new Customer("mohit2", "m2@gmail.com", "1234");
//        receiver.setCurrency(Currency.INR);
//        Wallet receiverWallet = new Wallet();
//        receiverWallet.setBalance(200.0);
//        receiver.setWallet(receiverWallet);
//        Mockito.when(customerRepo.findByEmail(sender.getEmail())).thenReturn(sender);
//        Mockito.when(customerRepo.findByEmail(receiver.getEmail())).thenReturn(receiver);
//        assertEquals("Money transfer successfull", customerService.addMoneyToOtherUserWallet(sender.getEmail(),receiver.getEmail(), oneDollar));
//        assertEquals(200.0 + 73.03, receiver.getWallet().getBalance());
//
//    }




}