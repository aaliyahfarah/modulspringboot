package com.example.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Asset;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.BrandRepository;
import com.example.demo.utils.CustomResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class AssetRestController {
    @Autowired private AssetRepository assetRepository;

    @GetMapping("asset")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> get() {
        return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", assetRepository.findAll());
    }

    @GetMapping("asset/{id}")
    public ResponseEntity<Object> get(@PathVariable(required = true) Integer id) {
        Asset asset = assetRepository.findById(id).orElse(null);
        if(asset != null){
            return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", asset);
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "data tidak ditemukan");
        }
    }
}