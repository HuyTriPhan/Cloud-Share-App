package com.example.cloudshare.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cloudshare.dto.PaymentDTO;
import com.example.cloudshare.dto.PaymentVerificationDTO;
import com.example.cloudshare.service.PaymentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order") 
    public ResponseEntity<?> createOrder(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO response = paymentService.createOrder(paymentDTO);
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationDTO request) {
        PaymentDTO response = paymentService.verifyPayment(request);
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> vnpayReturn(@RequestParam Map<String, String> params,
                                             HttpServletResponse httpResponse) throws IOException {
        PaymentVerificationDTO dto = new PaymentVerificationDTO();
        dto.setVnp_TxnRef(params.get("vnp_TxnRef"));
        dto.setVnp_Amount(params.get("vnp_Amount"));
        dto.setVnp_ResponseCode(params.get("vnp_ResponseCode"));
        dto.setVnp_TransactionNo(params.get("vnp_TransactionNo"));
        dto.setVnp_SecureHash(params.get("vnp_SecureHash"));
        dto.setVnp_OrderInfo(params.get("vnp_OrderInfo"));

        String orderInfo = params.getOrDefault("vnp_OrderInfo", "");
        String planId = orderInfo.contains("premium") ? "premium"
                      : orderInfo.contains("ultimate") ? "ultimate" : "";
        dto.setPlanId(planId);

        PaymentDTO result = paymentService.verifyPayment(dto);

        String redirectUrl = result.getSuccess()
            ? "http://localhost:5173/subscription?payment=success&credits=" + result.getCredits()
            : "http://localhost:5173/subscription?payment=failed";

        httpResponse.sendRedirect(redirectUrl);
        return ResponseEntity.status(302).build();
    }
}
