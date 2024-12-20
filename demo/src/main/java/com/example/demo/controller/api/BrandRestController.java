package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Brand;
import com.example.demo.repository.BrandRepository;
import com.example.demo.utils.CustomResponse;

@RestController
@RequestMapping("api")
public class BrandRestController {
    @Autowired private BrandRepository brandRepository;

    @GetMapping("brand")
    public List<Brand> get() {
        return brandRepository.findAll();
    }

    @GetMapping("brand/{id}")
    public Brand get(@PathVariable(required = true) Integer id) {
        return brandRepository.findById(id).orElse(null);
    }

    // create
    @PostMapping("brand/create")
    public ResponseEntity<Object> create(@RequestBody Brand brand) {
        brandRepository.save(brand);
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
    }

    //update
    @PutMapping("brand/edit/{id}")
    public ResponseEntity<Object> add(@RequestBody Brand brand) {
        Brand existingBrand = brandRepository.findById(brand.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID: " + brand.getId()));

        existingBrand.setName(brand.getName());
        brandRepository.save(existingBrand);
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
    }

    @DeleteMapping("brand/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable(required = true) Integer id){
        brandRepository.deleteById(id);
        if(brandRepository.findById(id).isEmpty() == true){
            return CustomResponse.generate(HttpStatus.OK, "data berhasil dihapus");
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "data gagal dihapus");
        }
    }
}
