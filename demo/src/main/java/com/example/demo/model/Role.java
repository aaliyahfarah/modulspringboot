package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_m_role")          
@Data                                  
@AllArgsConstructor                    
@NoArgsConstructor    
public class Role {
    @Id //PK ke type
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto increment
    private Integer id;
    private String name;
    private Integer level;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
