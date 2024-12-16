package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_m_vehicle")          // define sebuah table di database
@Data                                   // anotasI data, gantinya get set dari lombok
@AllArgsConstructor                     // constructor semua properti
@NoArgsConstructor                      //constructor yang tidak ada parameter
public class Vehicle {
    @Id //PK ke purchase detail
    private String id;
    private String addedAt;
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "tb_m_type_id", referencedColumnName = "id")
    private Type type;

    //one to one
}
