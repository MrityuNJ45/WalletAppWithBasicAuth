package com.example.MyPay3.repository;

import com.example.MyPay3.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    public Customer findByEmail(String email);
}
