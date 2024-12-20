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

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.utils.CustomResponse;

@RestController
@RequestMapping("api")
public class CategoryRestController {
    @Autowired private CategoryRepository categoryRepository;

    @GetMapping("category")
    public List<Category> get() {
        return categoryRepository.findAll();
    }

    @GetMapping("category/{id}")
    public Category get(@PathVariable(required = true) Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // create
    @PostMapping("/category/create")
    public ResponseEntity<Object> create(@RequestBody Category category) {
        categoryRepository.save(category);
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
    }

    //update
    @PutMapping("/category/edit/{id}")
    public ResponseEntity<Object> add(@RequestBody Category category) {
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + category.getId()));

        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
    }

    @DeleteMapping("category/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable(required = true) Integer id){
        categoryRepository.deleteById(id);
        if(categoryRepository.findById(id).isEmpty() == true){
            return CustomResponse.generate(HttpStatus.OK, "data berhasil dihapus");
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "data gagal dihapus");
        }
    }
}
