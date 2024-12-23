package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                                 // define sebuah table di database
@Table(name = "tb_m_type")              // untuk mapping
@Data                                   // anotasI data, gantinya get set dari lombok
@AllArgsConstructor                     // constructor semua properti
@NoArgsConstructor 
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto increment
    private Integer id;
    private String name;
    private Integer year;
    private Double price;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "tb_m_category_id", referencedColumnName = "id")
    private Category category;
    // category_id
    
    @ManyToOne
    @JoinColumn(name = "tb_m_brand_id", referencedColumnName = "id")
    private Brand brand;
    
    @OneToMany(mappedBy = "type")
    @JsonIgnore
    private List<Vehicle> vehicles;
}
