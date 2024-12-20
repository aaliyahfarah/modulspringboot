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

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.utils.CustomResponse;

@RestController
@RequestMapping("api")
public class CategoryRestController {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryRestController(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("category")
    public List<Category> get(){
        // model.addAttribute("categories", categoryRepository.findAll());
        return  categoryRepository.findAll();
    }

    @GetMapping(value = {"/category/add", "/category/{id}"})
    public ResponseEntity<Object> form(@PathVariable(required = false) Integer id, Model model){
        if(id != null){
            Category category = categoryRepository.findById(id).orElse(null);
            model.addAttribute("category", category);
            return CustomResponse.generate(HttpStatus.OK, "edit data");
        }
        else{
            model.addAttribute("category", new Category());
            return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody Category category) {
        if (category.getId() != null) {
            Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + category.getId()));

            existingCategory.setName(category.getName());
            categoryRepository.save(existingCategory);
        }
        else{
        categoryRepository.save(category);
        }
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan");
    }

    // @PostMapping("role")
    // public Role post(@RequestBody Role role) {
    //     return roleRepository.save(role);
    // }

    // @PostMapping("role")
    // public ResponseEntity<Object> post(@RequestBody Role role){
    //     return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan", roleRepository.save(role));
    // }

    // @DeleteMapping("role/{id}")
    // public Boolean delete(@PathVariable(required = true) Integer id){
    //     roleRepository.deleteById(id);
    //     return roleRepository.findById(id).isEmpty();
    // }

    // @DeleteMapping("role/{id}")
    // public ResponseEntity<Object> delete(@PathVariable(required = true) Integer id){
    //     roleRepository.deleteById(id);
    //     // Role role = roleRepository.findById(id).isEmpty();
    //     if(roleRepository.findById(id).isEmpty() == true){
    //         return CustomResponse.generate(HttpStatus.OK, "data berhasil dihapus");
    //     }
    //     else{
    //         return CustomResponse.generate(HttpStatus.OK, "data gagal dihapus");
    //     }
    // }
    
}
