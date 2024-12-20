package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Brand;
import com.example.demo.model.Category;
import com.example.demo.model.Type;
import com.example.demo.model.dto.TypeDTO;
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
    public TypeRestController(TypeRepository typeRepository, BrandRepository brandRepository, CategoryRepository categoryRepository ){
        this.typeRepository = typeRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    // @GetMapping("type")
    // public ResponseEntity<Object> get(Model model){
    //     // model.addAttribute("categories", categoryRepository.findAll());
    //     return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan",  typeRepository.findAll());
    // }

    @GetMapping("type")
    public List<Type> get(){
        // model.addAttribute("categories", categoryRepository.findAll());
        // return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan",  typeRepository.findAll());
        return typeRepository.findAll();
    }

    @GetMapping(value = {"/type/add", "/type/{id}"})
    public ResponseEntity<Object> form(@PathVariable(required = false) Integer id, Model model){
        if(id != null){
            Type type = typeRepository.findById(id).orElse(null);
            model.addAttribute("type", type);
            return CustomResponse.generate(HttpStatus.OK, "edit data");
        }
        else{
            model.addAttribute("type", new Type());
            return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
        }
    }

    @PostMapping("/type/save")
    public ResponseEntity<Object> save(@RequestBody TypeDTO typeDTO) {
        Brand brand = brandRepository.findByName(typeDTO.getBrand());
        Category category = categoryRepository.findByName(typeDTO.getCategory());

        if (typeDTO.getId() != null) {
            Type existingType = typeRepository.findById(typeDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + typeDTO.getId()));
            existingType.setName(typeDTO.getName());
            existingType.setYear(typeDTO.getYear());
            existingType.setPrice(typeDTO.getPrice());
            existingType.setBrand(brand);
            existingType.setCategory(category);
            typeRepository.save(existingType);
        }
        else{
            Type newType = new Type();
            newType.setName(typeDTO.getName());
            newType.setYear(typeDTO.getYear());
            newType.setPrice(typeDTO.getPrice());
            newType.setBrand(brand);
            newType.setCategory(category);
    
            typeRepository.save(newType);
        }
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
    }
}
