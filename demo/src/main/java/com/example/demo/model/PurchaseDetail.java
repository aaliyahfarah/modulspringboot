package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_tr_purchase_detail") // define sebuah table di database
@Data                                   // anotasI data, gantinya get set dari lombok
@AllArgsConstructor                     // constructor semua properti
@NoArgsConstructor 

public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double afterTaxPrice;
    // private String vehicleId;
    // private Integer purchaseId;

    //One to one
    //private Vehicle vehicle;

    //many to one purchase 
    @ManyToOne
    @JoinColumn(name = "tb_tr_purchase_id", referencedColumnName = "id")
    private Purchase purchase;

    //one to one vehicle
    //
}
