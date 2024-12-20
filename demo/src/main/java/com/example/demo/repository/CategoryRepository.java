package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
    Category findByName(String name);
    // Category findById(Integer id);
    // @Query("SELECT Category FROM Category ORDER BY t.id")
    // public List<TypeDTO> getUsingDTO();

    // @Query(value = "SELECT c.name FROM Category c")
    // public List<Category> getCategoryOption();

    // @Query("SELECT c.id FROM Category c WHERE c.name = :optionCategory ")
    // public Category getCategoryOption(@Param("optionCategory") String name );
}
