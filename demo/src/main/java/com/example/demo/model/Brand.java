package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_m_brand")          
@Data                                  
@AllArgsConstructor                    
@NoArgsConstructor    
public class Brand {
    @Id //PK ke type
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto increment
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<Type> types;
    // @JSonIgnore //buat json ignore
}
