package com.example.MyPay3.service;

import com.example.MyPay3.exceptions.WalletException;
import com.example.MyPay3.models.*;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.TransactionRepo;
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

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private Wallet wallet;
    @Mock
    private WalletRepo walletRepo;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void expectsToSaveACustomer() {

        Wallet wallet = new Wallet();
        Customer customer = Customer.builder().wallet(wallet).build();

        Mockito.when(walletRepo.save(Mockito.any(Wallet.class))).thenReturn(wallet);
        Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customer);

        Customer result = customerService.registerCustomer(customer);

        assertEquals(customer, result);
        Mockito.verify(walletRepo, Mockito.times(1)).save(Mockito.any(Wallet.class));
        Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
    }


    @Test
    public void expectsToAddMoneyToCustomerWallet() {
        String email = "test@example.com";
        MoneyDTO moneyDTO = new MoneyDTO(100.0, Currency.INR);
        Wallet wallet = Wallet.builder().balance(10.0).currency(Currency.INR).build();
        Customer customer = Customer.builder().wallet(wallet).build();
        Wallet updatedWallet = wallet.deposit(moneyDTO);
        Mockito.when(customerRepo.findByEmail(email)).thenReturn(customer);

        Mockito.when(walletRepo.save(Mockito.any(Wallet.class))).thenReturn(updatedWallet);
        Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customer);

        Customer result = customerService.addMoneyToCustomerWallet(email, moneyDTO);

        assertEquals(customer, result);
        Mockito.verify(customerRepo, Mockito.times(1)).findByEmail(email);

        Mockito.verify(walletRepo, Mockito.times(1)).save(Mockito.any(Wallet.class));
        Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
    }

    @Test
    public void expectsToWithdrawMoneyFromCustomerWallet() {

            String email = "test@example.com";
            MoneyDTO moneyDTO = new MoneyDTO(100.0, Currency.INR);
            Wallet wallet = Wallet.builder().balance(1000.0).currency(Currency.INR).build();
            Customer customer = Customer.builder().wallet(wallet).build();
            Wallet updatedWallet = wallet.deposit(moneyDTO);

            Mockito.when(customerRepo.findByEmail(email)).thenReturn(customer);

            Mockito.when(walletRepo.save(Mockito.any(Wallet.class))).thenReturn(updatedWallet);
            Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customer);

            Customer result = customerService.withdrawMoneyFromCustomerWallet(email, moneyDTO);

            assertEquals(customer, result);
            Mockito.verify(customerRepo, Mockito.times(1)).findByEmail(email);

            Mockito.verify(walletRepo, Mockito.times(1)).save(Mockito.any(Wallet.class));
            Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));

    }


    @Test
    public void expectsToAddMoneyToAnotherPerson() {
        // Mock data
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";
        MoneyDTO moneyToSend = new MoneyDTO(10.0,Currency.INR);

        Wallet senderWallet = Wallet.builder().balance(120.0).currency(Currency.INR).build();

        Customer senderCustomer = Customer.builder().email(senderEmail).wallet(senderWallet).build();
        // Mock receiver customer
        Wallet receiverWallet = Wallet.builder().balance(120.0).currency(Currency.INR).build();
        Customer receiverCustomer = Customer.builder().email(receiverEmail).wallet(receiverWallet).build();        // Mock customerRepo.findByEmail()
        Mockito.when(customerRepo.findByEmail(senderEmail)).thenReturn(senderCustomer);
        Mockito.when(customerRepo.findByEmail(receiverEmail)).thenReturn(receiverCustomer);

        // Invoke the method
        MoneyTransfer actual = customerService.addMoneyToOtherUserWallet(senderEmail, receiverEmail, moneyToSend);

        // Assertions
        senderWallet = senderWallet.deposit(moneyToSend);
        receiverWallet = receiverWallet.withdraw(moneyToSend);

        Mockito.verify(walletRepo, Mockito.times(1)).save(senderWallet);
        Mockito.verify(walletRepo, Mockito.times(1)).save(receiverWallet);
        Mockito.verify(customerRepo, Mockito.times(1)).save(senderCustomer);
        Mockito.verify(customerRepo, Mockito.times(1)).save(receiverCustomer);
        Mockito.verify(transactionRepo, Mockito.times(1)).save(Mockito.any(Transaction.class));
        MoneyTransfer expected = new MoneyTransfer(true);
        assertEquals(expected, actual);
    }


    @Test
    public void expectsToThrowExceptionWhenTransferringToAnotherPersonWhileReceivingPersonIsHavingInsufficientBalance() {
        // Mock data
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";
        MoneyDTO moneyToSend = new MoneyDTO(10.0,Currency.INR);

        Wallet senderWallet = Wallet.builder().balance(5.0).currency(Currency.INR).build();

        Customer senderCustomer = Customer.builder().email(senderEmail).wallet(senderWallet).build();
        // Mock receiver customer
        Wallet receiverWallet = Wallet.builder().balance(120.0).currency(Currency.INR).build();
        Customer receiverCustomer = Customer.builder().email(receiverEmail).wallet(receiverWallet).build();
        Mockito.when(customerRepo.findByEmail(senderEmail)).thenReturn(senderCustomer);
        Mockito.when(customerRepo.findByEmail(receiverEmail)).thenReturn(receiverCustomer);

        // Invoke the method
        WalletException thrown = assertThrows(WalletException.class, () -> {
            customerService.addMoneyToOtherUserWallet(senderEmail, receiverEmail, moneyToSend);
        }) ;

        assertEquals(thrown.getMessage(), "Insufficient balance");

    }


}