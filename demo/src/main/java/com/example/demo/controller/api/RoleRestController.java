package com.example.demo.controller.api;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.utils.CustomResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api")
public class RoleRestController {
    private RoleRepository roleRepository;

    @Autowired
    public RoleRestController(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    // @GetMapping("role")
    // public List<Role> get(){
    //     return roleRepository.findAll();
    // }

    @GetMapping("role")
    public ResponseEntity<Object> get(){
        return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", roleRepository.findAll());
    }

    // @GetMapping("role/{id}")
    // public Role get(@PathVariable(required = true) Integer id) {
    //     return roleRepository.findById(id).orElse(null);
    // }

    @GetMapping("role/{id}")
    public ResponseEntity<Object> get(@PathVariable(required = true) Integer id){
        Role role = roleRepository.findById(id).orElse(null);
        if(role != null){
            return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", role);
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "data tidak ditemukan");
        }
    }

    // @PostMapping("role")
    // public Role post(@RequestBody Role role) {
    //     return roleRepository.save(role);
    // }

    @PostMapping("role")
    public ResponseEntity<Object> post(@RequestBody Role role){
        return CustomResponse.generate(HttpStatus.OK, "data berhasil ditambahkan", roleRepository.save(role));
    }

    // @DeleteMapping("role/{id}")
    // public Boolean delete(@PathVariable(required = true) Integer id){
    //     roleRepository.deleteById(id);
    //     return roleRepository.findById(id).isEmpty();
    // }

    @DeleteMapping("role/{id}")
    public ResponseEntity<Object> delete(@PathVariable(required = true) Integer id){
        roleRepository.deleteById(id);
        // Role role = roleRepository.findById(id).isEmpty();
        if(roleRepository.findById(id).isEmpty() == true){
            return CustomResponse.generate(HttpStatus.OK, "data berhasil dihapus");
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "data gagal dihapus");
        }
    }
    
}