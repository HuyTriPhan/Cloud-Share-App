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
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_PayDate;
    private String vnp_TransactionStatus;
}