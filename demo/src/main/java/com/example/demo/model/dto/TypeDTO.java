package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class TypeDTO { // untuk select
    private Integer id;
    private String name;
    private Integer year;
    private Double price;
    private String brand;
    private String category;
}
