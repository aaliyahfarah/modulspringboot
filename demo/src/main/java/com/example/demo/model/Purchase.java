package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_tr_purchase")           // define sebuah table di database
@Data                                      // anotasI data, gantinya get set dari lombok
@AllArgsConstructor                        // constructor dengan parameter semua properti
@NoArgsConstructor                         // constructor dengan tidak ada parameter

public class Purchase {
    @Id //pk ke pdetail
    private String id;
    private String date;
    private Double total_amount;

    @ManyToOne
    @JoinColumn(name = "tb_m_user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tb_m_payment_method_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod; 

    @OneToMany(mappedBy = "purchase")
    private List<PurchaseDetail> purchaseDetails;
}
