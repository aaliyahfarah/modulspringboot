package com.example.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.PurchaseDetailRepository;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.utils.CustomResponse;

@RestController
@RequestMapping("api/purchase-detail")
public class PurchaseDetailRestController {
    private PurchaseRepository purchaseRepository;
    private VehicleRepository vehicleRepository;
    private PurchaseDetailRepository purchaseDetailRepository;

    @Autowired
    public PurchaseDetailRestController(PurchaseDetailRepository purchaseDetailRepository,
    PurchaseRepository purchaseRepository, VehicleRepository vehicleRepository){
        this.purchaseRepository = purchaseRepository;
        this.vehicleRepository = vehicleRepository;
        this.purchaseDetailRepository = purchaseDetailRepository;
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", purchaseDetailRepository.findAll());
    }

    



}
