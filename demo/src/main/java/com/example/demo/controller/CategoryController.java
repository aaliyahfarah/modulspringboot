package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("category")
public class CategoryController {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    //localhost:8080/category -> index
    @GetMapping
    public String index(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "category/index"; //returnnya path bukan value
    }

    //localhost:8080/category/form -> create dan edit
    @GetMapping(value = {"form", "form/{id}"})
    public String form(@PathVariable(required = false) Integer id, Model model) {
        if(id != null){
            Category category = categoryRepository.findById(id).orElse(null);
            model.addAttribute("category", category);
        }
        else{
            model.addAttribute("category", new Category());
        }
        return "category/form";
    }

    //localhost:8080/category/form/{id} -> update
    // @GetMapping("edit/{id}")
    // public String editForm(@PathVariable Integer id, Model model) {
    //     Category category = categoryRepository.findById(id)
    //         .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));
    //     model.addAttribute("category", category);
    //     return "category/edit"; 
    // }

    //localhost:8080/category/save
    @PostMapping("save")
    public String save(Category category) {
        if (category.getId() != null) {
            Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + category.getId()));

            existingCategory.setName(category.getName());
            categoryRepository.save(existingCategory);
        }
        else{
        categoryRepository.save(category);
        }
        return "redirect:/category";
    }

    //localhost:8080/category/delete{id} -> delete
    // @PostMapping("delete/{id}")
    // public String delete(@PathVariable Integer id, Model model) {
    //     Category existingCategory = categoryRepository.findById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));

    //     categoryRepository.delete(existingCategory);
    //     return "redirect:/category";
    // }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        categoryRepository.deleteById(id);
        // if(!categoryRepository.findById(id).isPresent()){
        //     return "redirect:/category";
        // }
        // Contoh gagal: di front end kasih alert
        return "redirect:/category";
    }    

    
}