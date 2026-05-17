package com.example.cloudshare.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.cloudshare.domain.PaymentTransaction;

public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {

    List<PaymentTransaction> findByClerkId(String clerkId);

    List<PaymentTransaction> findByClerkIdOrderByTransactionDateDesc(String clerkId);

    List<PaymentTransaction> findByClerkIdAndStatusOrderByTransactionDateDesc(String clerkId, String status);
}
