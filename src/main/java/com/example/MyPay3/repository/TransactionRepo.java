package com.example.MyPay3.repository;

import com.example.MyPay3.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);
}
