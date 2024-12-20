package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Brand;
import com.example.demo.repository.BrandRepository;

@RestController
@RequestMapping("api")
public class BrandRestController {
    private BrandRepository brandRepository;

    @Autowired
    public BrandRestController(BrandRepository brandRepository){
        this.brandRepository = brandRepository;
    }

    @GetMapping("brand")
    public List<Brand> get(){
        return  brandRepository.findAll();
    }
}
