package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Purchase;
import com.example.demo.model.dto.PurchaseDTO;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String>{
    // @Query("SELECT new com.example.demo.model.dto.ProfileDTO(u.id, p.firstName, p.lastName, p.telephone, p.email, p.address, u.password) FROM User u JOIN u.person p WHERE u.id = :userId")
    // ProfileDTO getUsingProfileDTO(@Param(value= "userId") Integer id);

    //list
//     SELECT p.id, p.date, p.total_amount, pn.first_name, pn.last_name, m.name
// FROM tb_tr_purchase p
// JOIN tb_m_payment_method m ON p.tb_m_payment_method_id = m.id
// JOIN tb_m_user u ON p.tb_m_user_id = u.id
// JOIN tb_m_person pn ON pn.id = u.id
// WHERE pn.first_name LIKE '%jane%' OR pn.last_name;

    @Query("SELECT new com.example.demo.model.dto.PurchaseDTO(p.id, p.date," +
    " pn.firstName, pn.lastName, p.totalAmount, m.name) FROM Purchase p" +
    " JOIN p.paymentMethod m JOIN p.user u JOIN u.person pn" + 
    " WHERE" +
    " p.id LIKE CONCAT('%',:search,'%')" +
    " OR p.date LIKE CONCAT('%',:search,'%')" +
    " OR CAST( p.totalAmount AS string ) LIKE CONCAT('%',:search,'%')" +
    " OR pn.firstName LIKE CONCAT('%',:search,'%')" +
    " OR pn.lastName LIKE CONCAT('%',:search,'%')" +
    " OR m.name LIKE CONCAT('%',:search,'%')" 
    )  
    Page<PurchaseDTO> findPurchase(@Param("search") String name, Pageable pageable);
    // @Query("SELECT p FROM Purchase p WHERE ")
    

    @Query(value = "SELECT COUNT(*) FROM Purchase p " +
    " JOIN p.paymentMethod m JOIN p.user u JOIN u.person pn" + 
    " WHERE p.id LIKE CONCAT('%',:search,'%')" +
    " OR p.date LIKE CONCAT('%',:search,'%')" +
    " OR p.totalAmount LIKE CONCAT('%',:search,'%')" +
    " OR pn.firstName LIKE CONCAT('%',:search,'%')" +
    " OR pn.lastName LIKE CONCAT('%',:search,'%')" +
    " OR m.name LIKE CONCAT('%',:search,'%') ")
    long countFiltered(@Param("search") String search);


    // List<PurchaseDTO> searchPurchaseByName(@Param("search") String name);


    


}
