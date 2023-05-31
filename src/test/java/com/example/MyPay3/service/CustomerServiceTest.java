package com.example.MyPay3.service;

import com.example.MyPay3.exceptions.TransactionException;
import com.example.MyPay3.exceptions.WalletException;
import com.example.MyPay3.models.*;
import com.example.MyPay3.repository.CustomerRepo;
import com.example.MyPay3.repository.TransactionRepo;
import com.example.MyPay3.repository.WalletRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        Customer customer = Customer.builder().name("manish").email("m@gmail.com").build();
        Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(customer);
        Customer result = customerService.registerCustomer(customer);
        assertEquals(customer, result);
        Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
    }


    @Test
    public void expectsToAddMoneyToCustomerWallet() {
        String email = "test@example.com";
        WalletDTO moneyDTO = new WalletDTO(100.0, Currency.INR);
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
            WalletDTO moneyDTO = new WalletDTO(100.0, Currency.INR);
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
        WalletDTO moneyToSend = new WalletDTO(10.0,Currency.INR);

        Wallet senderWallet = Wallet.builder().balance(120.0).currency(Currency.INR).build();

        Customer senderCustomer = Customer.builder().email(senderEmail).wallet(senderWallet).build();
        // Mock receiver customer
        Wallet receiverWallet = Wallet.builder().balance(120.0).currency(Currency.INR).build();
        Customer receiverCustomer = Customer.builder().email(receiverEmail).wallet(receiverWallet).build();        // Mock customerRepo.findByEmail()
        Mockito.when(customerRepo.findByEmail(senderEmail)).thenReturn(senderCustomer);
        Mockito.when(customerRepo.findByEmail(receiverEmail)).thenReturn(receiverCustomer);

        // Invoke the method
        MoneyTransferResponse actual = customerService.addMoneyToOtherUserWallet(senderEmail, receiverEmail, moneyToSend);

        // Assertions
        senderWallet = senderWallet.deposit(moneyToSend);
        receiverWallet = receiverWallet.withdraw(moneyToSend);

        Mockito.verify(walletRepo, Mockito.times(1)).save(senderWallet);
        Mockito.verify(walletRepo, Mockito.times(1)).save(receiverWallet);
        Mockito.verify(customerRepo, Mockito.times(1)).save(senderCustomer);
        Mockito.verify(customerRepo, Mockito.times(1)).save(receiverCustomer);
        Mockito.verify(transactionRepo, Mockito.times(1)).save(Mockito.any(Transaction.class));
        MoneyTransferResponse expected = new MoneyTransferResponse(true);
        assertEquals(expected, actual);
    }


    @Test
    public void expectsToThrowExceptionWhenTransferringToAnotherPersonWhileSendingPersonIsHavingInsufficientBalance() {

        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";
        WalletDTO moneyToSend = new WalletDTO(10.0,Currency.INR);

        Wallet senderWallet = Wallet.builder().balance(5.0).currency(Currency.INR).build();

        Customer senderCustomer = Customer.builder().email(senderEmail).wallet(senderWallet).build();

        Wallet receiverWallet = Wallet.builder().balance(120.0).currency(Currency.INR).build();
        Customer receiverCustomer = Customer.builder().email(receiverEmail).wallet(receiverWallet).build();
        Mockito.when(customerRepo.findByEmail(senderEmail)).thenReturn(senderCustomer);
        Mockito.when(customerRepo.findByEmail(receiverEmail)).thenReturn(receiverCustomer);


        WalletException thrown = assertThrows(WalletException.class, () -> {
            customerService.addMoneyToOtherUserWallet(senderEmail, receiverEmail, moneyToSend);
        }) ;

        assertEquals(thrown.getMessage(), "Insufficient balance in sender's wallet");

    }

    @Test
    public void testGetTransactionHistoryByUserEmail_Success() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setEmail("test@example.com");

        Transaction transaction1 = new Transaction(1,1,2,20.0);
        Transaction transaction2 = new Transaction(2,3,1,40.0);


        Mockito.when(customerRepo.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(transactionRepo.findBySenderIdOrReceiverId(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Arrays.asList(transaction1, transaction2));


        List<Transaction> result = customerService.getTransactionHistoryByUserEmail("test@example.com");


        assertNotNull(result);
        assertEquals(2, result.size());

    }

    @Test
    public void testGetTransactionHistoryByUserEmail_NoTransactionsFound() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setEmail("test@example.com");


        Mockito.when(customerRepo.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(transactionRepo.findBySenderIdOrReceiverId(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());

        TransactionException thrown = assertThrows(TransactionException.class, () -> {
            customerService.getTransactionHistoryByUserEmail("test@example.com");
        });
        assertEquals(thrown.getMessage(), "No transactions found");
    }


    @Test
    public void testAddWalletToUser_Success() {

        Wallet wallet = new Wallet();
        Customer customer = new Customer();
        Customer walletAddedCustomer = new Customer(wallet);

        Mockito.when(customerRepo.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(customerRepo.save(Mockito.any(Customer.class))).thenReturn(walletAddedCustomer);
        Mockito.when(walletRepo.save(Mockito.any(Wallet.class))).thenReturn(wallet);

        Wallet result = customerService.addWalletToUser("test@example.com", wallet);

        Mockito.verify(customerRepo, Mockito.times(1)).findByEmail("test@example.com");
        Mockito.verify(customerRepo, Mockito.times(1)).save(Mockito.any(Customer.class));
        Mockito.verify(walletRepo, Mockito.times(1)).save(Mockito.any(Wallet.class));

        assertNotNull(result);
        assertTrue(walletAddedCustomer.isWalletAdded());
        assertEquals(wallet, result);
    }




}