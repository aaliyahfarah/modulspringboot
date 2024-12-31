package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_m_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Integer id;
    private String password; 

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id") 
    private Person person; 

    @ManyToOne
    @JoinColumn(name = "tb_m_role_id", referencedColumnName = "id") 
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<PasswordResetToken> passwordResetTokens;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Purchase> purchases;
}
