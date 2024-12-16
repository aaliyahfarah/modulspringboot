package com.example.demo.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.RegisterDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("user-management")
public class UserManagementController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "user-management/login";
    }

    @PostMapping("/authenticate")
    public String authenticate(LoginDTO login) {
        UserDTO user = userRepository.getUsingDTO(login.getEmail());
        if(user != null && passwordEncoder.matches(login.getPassword(), user.getPassword())){
            try {
                org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                    user.getId().toString(),
                    "",
                    getAuthorities(user.getRoleName()));
                PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
                    user, 
                    "", 
                    userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return "redirect:/dashboard";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return "redirect:/user-management";
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(String role){
        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }   

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "user-management/register";
    }

    @PostMapping("/")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
