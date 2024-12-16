package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;
import com.example.demo.model.dto.UserDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    @Query("SELECT new com.example.demo.model.dto.UserDTO(u.id, p.firstName, p.lastName, r.name, u.password) FROM User u JOIN u.role r JOIN u.person p WHERE p.email = :userEmail")
    UserDTO getUsingDTO(@Param(value= "userEmail") String email);

}

