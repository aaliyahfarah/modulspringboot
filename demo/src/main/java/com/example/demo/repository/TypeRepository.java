package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Type;
import com.example.demo.model.dto.TypeDTO;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer>{
    @Query("SELECT new com.example.demo.model.dto.TypeDTO(t.id, t.name, t.year, t.price, b.name, c.name) FROM Type t JOIN t.category c JOIN t.brand b ORDER BY t.id")
    public List<TypeDTO> getAllTypeDTO();

    @Query("SELECT new com.example.demo.model.dto.TypeDTO(t.id, t.name, t.year, t.price, b.name, c.name) FROM Type t JOIN t.category c JOIN t.brand b WHERE t.id = :idType ORDER BY t.id")
    public TypeDTO getIdTypeDTO(@Param("idType") Integer id);

}
