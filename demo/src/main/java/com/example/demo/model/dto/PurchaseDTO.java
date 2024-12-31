package com.example.demo.model.dto;

import java.sql.Date;
import java.util.List;

import com.example.demo.model.PurchaseDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// public class PurchaseDTO {
//     private 
// }
@Data
@AllArgsConstructor
@NoArgsConstructor 
public class PurchaseDTO {
    private String id;
    private String date;
    private String firstName;
    private String lastName;
    private Double totalAmount;
    private String paymentMethod;
    // private List<PurchaseDetail> purchaseDetails; 
} 