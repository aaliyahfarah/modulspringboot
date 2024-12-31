package com.example.demo.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.model.dto.PurchaseDTO;
import com.example.demo.repository.PurchaseDetailRepository;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.utils.CustomResponse;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api")
public class PurchaseRestController {
    private PurchaseRepository purchaseRepository;
    private VehicleRepository vehicleRepository;
    private PurchaseDetailRepository purchaseDetailRepository;

    @Autowired
    public PurchaseRestController(PurchaseDetailRepository purchaseDetailRepository,
            PurchaseRepository purchaseRepository, VehicleRepository vehicleRepository) {
        this.purchaseRepository = purchaseRepository;
        this.vehicleRepository = vehicleRepository;
        this.purchaseDetailRepository = purchaseDetailRepository;
    }

    // @GetMapping("purchase")
    // public ResponseEntity<Object> get() {
    // return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan",
    // purchaseRepository.findAll());
    // }

    // @GetMapping("purchase/{search}")
    // public ResponseEntity<Object> get(@PathVariable(required = true) String
    // search) {
    // Pageable
    // Page<purchaseDTO> purchaseDTO = purchaseRepository.findPurchase(search,
    // pageable);

    // if (purchaseDTO != null) {
    // return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan",
    // purchaseDTO);
    // } else {
    // return CustomResponse.generate(HttpStatus.OK, "data tidak ditemukan");
    // }
    // }

    @GetMapping("purchase")
    public Map<String, Object> getPurchases(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam(value = "search", defaultValue = "") String search) {

        int page = start / length;

        Pageable pageable = PageRequest.of(page, length);
        Page<PurchaseDTO> purchases = purchaseRepository.findPurchase(search, pageable);

        long totalRecords = getTotalRecords();
        long filteredRecords = getFilteredRecordsCount(search);

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", totalRecords);
        response.put("recordsFiltered", filteredRecords);
        response.put("data", purchases.getContent());

        return response;
    }

    private long getTotalRecords() {
        return purchaseRepository.count();
    }

    private long getFilteredRecordsCount(String search) {
        return purchaseRepository.countFiltered(search);
    }

}

// @GetMapping("category/{id}")
// public ResponseEntity<Object> get(@PathVariable(required = true) Integer id)
// {
// Category category = categoryRepository.findById(id).orElse(null);
// if(category != null){
// return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan",
// category);
// }
// else{
// return CustomResponse.generate(HttpStatus.OK, "data tidak ditemukan");
// }
// }
