package com.example.cloudshare.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.cloudshare.domain.PaymentTransaction;
import com.example.cloudshare.domain.Profile;
import com.example.cloudshare.repository.PaymentTransactionRepository;
import com.example.cloudshare.service.ProfileService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getUserTransactions() {
        Profile currentProfile = profileService.getCurrentProfile();
        String clerkId = currentProfile.getClerkId();

        List<PaymentTransaction> transactions = paymentTransactionRepository
                .findByClerkIdAndStatusOrderByTransactionDateDesc(clerkId, "SUCCESS");
        return ResponseEntity.ok(transactions);
    }
}
