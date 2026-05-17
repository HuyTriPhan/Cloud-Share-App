package com.example.cloudshare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerificationDTO {
    private String vnp_TxnRef;        
    private String vnp_Amount;
    private String vnp_ResponseCode; 
    private String vnp_TransactionNo; 
    private String vnp_SecureHash;  
    private String vnp_OrderInfo;   
    private String planId;    
    // Thêm đủ các field VNPay trả về nếu cần
}