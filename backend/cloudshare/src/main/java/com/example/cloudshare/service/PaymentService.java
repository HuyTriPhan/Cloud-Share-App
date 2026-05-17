package com.example.cloudshare.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.cloudshare.domain.PaymentTransaction;
import com.example.cloudshare.domain.Profile;
import com.example.cloudshare.dto.PaymentDTO;
import com.example.cloudshare.dto.PaymentVerificationDTO;
import com.example.cloudshare.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ProfileService profileService;
    private final UserCreditsService userCreditsService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @Value("${vnpay.tmn-code}")
    private String vnpTmnCode;

    @Value("${vnpay.hash-secret}")
    private String vnpHashSecret;

    @Value("${vnpay.pay-url}")
    private String vnpPayUrl;

    @Value("${vnpay.return-url}")
    private String vnpReturnUrl;

    // Giữ nguyên signature method nhưng đổi sang HEX (VNPay dùng hex, không phải base64)
    public PaymentDTO createOrder(PaymentDTO paymentDTO) {
        try {
            Profile currentProfile = profileService.getCurrentProfile();
            String clerkId = currentProfile.getClerkId();
            String orderId = "ORD" + System.currentTimeMillis();

            // Lưu pending transaction (giữ nguyên như cũ)
            PaymentTransaction transaction = PaymentTransaction.builder()
                    .clerkId(clerkId)
                    .orderId(orderId)
                    .planId(paymentDTO.getPlanId())
                    .amount(paymentDTO.getAmount())
                    .currency("VND")
                    .status("PENDING")
                    .transactionDate(LocalDateTime.now())
                    .userEmail(currentProfile.getEmail())
                    .userName(currentProfile.getFirstName() + " " + currentProfile.getLastName())
                    .build();
            paymentTransactionRepository.save(transaction);

            // Build VNPay payment URL
            String paymentUrl = buildVnpayUrl(orderId, paymentDTO.getAmount(), paymentDTO.getPlanId());

            return PaymentDTO.builder()
                    .orderId(orderId)
                    .success(true)
                    .message(paymentUrl) // trả URL về cho frontend redirect
                    .build();

        } catch (Exception e) {
            return PaymentDTO.builder()
                    .success(false)
                    .message("Error creating order: " + e.getMessage())
                    .build();
        }
    }

    public PaymentDTO verifyPayment(PaymentVerificationDTO request) {
        try {
            if (!isValidSignature(request)) {
                updateTransactionStatus(request.getVnp_TxnRef(), "FAILED",
                        request.getVnp_TransactionNo(), null);
                return PaymentDTO.builder()
                        .success(false)
                        .message("Payment signature verification failed")
                        .build();
            }
            if (!"00".equals(request.getVnp_ResponseCode())) {
                updateTransactionStatus(request.getVnp_TxnRef(), "FAILED",
                        request.getVnp_TransactionNo(), null);
                return PaymentDTO.builder()
                        .success(false)
                        .message("Payment failed with code: " + request.getVnp_ResponseCode())
                        .build();
            }

            String planId = request.getPlanId();
            Profile currentProfile = profileService.getCurrentProfile();
            String clerkId = currentProfile.getClerkId();

            int creditsToAdd = 0;
            String plan = "BASIC";
            switch (planId) {
                case "premium":
                    creditsToAdd = 500;
                    plan = "PREMIUM";
                    break;
                case "ultimate":
                    creditsToAdd = 5000;
                    plan = "ULTIMATE";
                    break;
            }

            if (creditsToAdd > 0) {
                userCreditsService.addCredits(clerkId, creditsToAdd, plan);
                updateTransactionStatus(request.getVnp_TxnRef(), "SUCCESS",
                        request.getVnp_TransactionNo(), creditsToAdd);
                return PaymentDTO.builder()
                        .success(true)
                        .message("Payment verified and credits added successfully")
                        .credits(userCreditsService.getUserCredits(clerkId).getCredits())
                        .build();
            } else {
                updateTransactionStatus(request.getVnp_TxnRef(), "FAILED",
                        request.getVnp_TransactionNo(), null);
                return PaymentDTO.builder()
                        .success(false)
                        .message("Invalid plan selected")
                        .build();
            }

        } catch (Exception e) {
            try {
                updateTransactionStatus(request.getVnp_TxnRef(), "ERROR",
                        request.getVnp_TransactionNo(), null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return PaymentDTO.builder()
                    .success(false)
                    .message("Error verifying payment: " + e.getMessage())
                    .build();
        }
    }

    private void updateTransactionStatus(String orderId, String status,
            String paymentId, Integer creditsToAdd) {
        paymentTransactionRepository.findAll().stream()
                .filter(t -> t.getOrderId() != null && t.getOrderId().equals(orderId))
                .findFirst()
                .map(transaction -> {
                    transaction.setStatus(status);
                    transaction.setPaymentId(paymentId);
                    if (creditsToAdd != null) {
                        transaction.setCreditsAdded(creditsToAdd);
                    }
                    return paymentTransactionRepository.save(transaction);
                });
    }

    private String buildVnpayUrl(String orderId, int amount, String planId) throws Exception {
        Map<String, String> params = new TreeMap<>(); // TreeMap để tự sort theo key
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnpTmnCode);
        params.put("vnp_Amount", String.valueOf(amount * 100L)); // VNPay nhân 100
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", "Thanh toan " + planId + " plan");
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnpReturnUrl);
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            hashData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                    .append("&");
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                    .append("&");
        }

        String hashDataStr = hashData.substring(0, hashData.length() - 1);
        String queryStr = query.substring(0, query.length() - 1);

        String secureHash = generateHmacSha256Hex(hashDataStr, vnpHashSecret);
        return vnpPayUrl + "?" + queryStr + "&vnp_SecureHash=" + secureHash;
    }

    private boolean isValidSignature(PaymentVerificationDTO request) throws Exception {

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Amount", request.getVnp_Amount());
        params.put("vnp_ResponseCode", request.getVnp_ResponseCode());
        params.put("vnp_TransactionNo", request.getVnp_TransactionNo());
        params.put("vnp_TxnRef", request.getVnp_TxnRef());
        params.put("vnp_OrderInfo", request.getVnp_OrderInfo());

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                hashData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                        .append("&");
            }
        }
        String hashDataStr = hashData.substring(0, hashData.length() - 1);
        String generatedHash = generateHmacSha256Hex(hashDataStr, vnpHashSecret);
        return generatedHash.equals(request.getVnp_SecureHash());
    }

    private String generateHmacSha256Hex(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}