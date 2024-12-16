package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Brand;
import com.example.demo.model.Category;
import com.example.demo.model.Type;
import com.example.demo.model.dto.TypeDTO;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TypeRepository;


@Controller
@RequestMapping("type")
public class TypeController {
    private TypeRepository typeRepository;
    private CategoryRepository categoryRepository;
    private BrandRepository brandRepository;

    @Autowired
    public TypeController(TypeRepository typeRepository, BrandRepository brandRepository, CategoryRepository categoryRepository ){
        this.typeRepository = typeRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("types", typeRepository.getAllTypeDTO());
        return "type/index";
    }

    @GetMapping(value = {"form", "form/{id}"})
    public String form(@PathVariable(required = false) Integer id, Model model) {

        // Fetching all brands and categories to be displayed in the form
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());

        if (id != null) {
            // Fetch TypeDTO based on the provided id
            model.addAttribute("typeDto", typeRepository.getIdTypeDTO(id));
        } else {
            model.addAttribute("typeDto", new TypeDTO());
        }

        return "type/form";
    }


    @PostMapping("save")
    public String save(TypeDTO typeDTO, Model model) {
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
        return "redirect:/type";
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        typeRepository.deleteById(id);
        return "redirect:/type";
    } 
}