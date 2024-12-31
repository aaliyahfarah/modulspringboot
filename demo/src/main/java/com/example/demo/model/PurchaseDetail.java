package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_tr_purchase_detail") // define sebuah table di database
@Data                                   // anotasI data, gantinya get set dari lombok
@AllArgsConstructor                     // constructor semua properti
@NoArgsConstructor 

public class PurchaseDetail {
    @Id
    private String vehicleId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "tb_m_vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    private Double afterTaxPrice;

    //many to one purchase 
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tb_tr_purchase_id", referencedColumnName = "id")
    private Purchase purchase;

}
