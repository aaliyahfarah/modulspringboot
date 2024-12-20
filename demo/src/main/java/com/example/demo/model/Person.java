package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_m_person")
@Data                                  
@AllArgsConstructor                    
@NoArgsConstructor  
public class Person {
    @Id //PK ke user
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private String address;

    @OneToOne(mappedBy = "person")
    @JsonIgnore
    private User user;
}