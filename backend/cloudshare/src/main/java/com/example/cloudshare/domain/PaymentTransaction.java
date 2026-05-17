package com.example.cloudshare.domain;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "payment_transactions")
public class PaymentTransaction {
    private String id;
    private String clerkId;
    private String orderId;
    private String paymentId;
    private String planId;
    private int amount;
    private String currency;
    private int creditsAdded;
    private String status;
    private LocalDateTime transactionDate;

    private String userEmail;
    private String userName;
}
