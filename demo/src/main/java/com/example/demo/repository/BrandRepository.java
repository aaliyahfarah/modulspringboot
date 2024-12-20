package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    // dependencies injection - dirun sekali bisa dipake berkali2

    // @Query("SELECT b FROM Brand b")
    // public List<Brand> getBrandNameOption();

    // @Query("SELECT b.id FROM Brand b WHERE b.id = :optionBrand")
    // public Brand getBrandOption(@Param("optionBrand") String name);

    Brand findByName(String name);
    
}
