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

@Entity(name = "tb_m_asset")          
@Data                                  
@AllArgsConstructor                    
@NoArgsConstructor    
public class Asset {
    @Id //PK ke type
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto increment
    private Integer id;
    private String path;

    @OneToMany(mappedBy = "asset")
    @JsonIgnore
    private List<Type> types;
}