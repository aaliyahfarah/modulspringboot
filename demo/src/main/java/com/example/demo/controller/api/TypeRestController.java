package com.example.demo.controller.api;

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
import com.example.demo.model.Category;
import com.example.demo.model.Type;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.utils.CustomResponse;

@RestController
@RequestMapping("api")
public class TypeRestController {
    private TypeRepository typeRepository;
    private CategoryRepository categoryRepository;
    private BrandRepository brandRepository;

    @Autowired
    public TypeRestController(TypeRepository typeRepository, BrandRepository brandRepository,
            CategoryRepository categoryRepository) {
        this.typeRepository = typeRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping("type")
    public ResponseEntity<Object> get() {
        return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", typeRepository.findAll());
    }

    @GetMapping("type/{id}")
    public ResponseEntity<Object> get(@PathVariable(required = true) Integer id) {
        Type type = typeRepository.findById(id).orElse(null);
        if(type != null){
            return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", type);
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "data tidak ditemukan");
        }
    }


    // create
    @PostMapping("type/create")
    public ResponseEntity<Object> create(@RequestBody Type type) {
        Brand brand = brandRepository.findById(type.getBrand().getId()).orElse(null);
        Category category = categoryRepository.findById(type.getCategory().getId()).orElse(null);

        Type newType = new Type();
        newType.setName(type.getName());
        newType.setYear(type.getYear());
        newType.setPrice(type.getPrice());
        newType.setBrand(brand);
        newType.setCategory(category);

        typeRepository.save(newType);
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
    }

    // edit
    @PutMapping("type/edit/{id}")
    public ResponseEntity<Object> edit(@RequestBody Type type) {
        Brand brand = brandRepository.findById(type.getBrand().getId()).orElse(null);
        Category category = categoryRepository.findById(type.getCategory().getId()).orElse(null);

        Type existingType = typeRepository.findById(type.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid type ID: " + type.getId()));
        existingType.setName(type.getName());
        existingType.setYear(type.getYear());
        existingType.setPrice(type.getPrice());
        existingType.setBrand(brand);
        existingType.setCategory(category);
        typeRepository.save(existingType);
        return CustomResponse.generate(HttpStatus.OK, "data berhasil diubah");
    }

    //delete
    @DeleteMapping("type/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable(required = true) Integer id){
        typeRepository.deleteById(id);
        if(typeRepository.findById(id).isEmpty() == true){
            return CustomResponse.generate(HttpStatus.OK, "data berhasil dihapus");
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "data gagal dihapus");
        }
    }
}
