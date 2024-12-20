package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String telephone;
    private String address;
    private String email;
    private String password;
}
