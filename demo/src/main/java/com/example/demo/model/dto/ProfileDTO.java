package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ProfileDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private String address;
    private String password;
}