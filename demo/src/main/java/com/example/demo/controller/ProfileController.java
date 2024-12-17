package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.dto.ProfileDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("profile")
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
    private PersonRepository personRepository;

    public ProfileController(UserRepository userRepository, PersonRepository personRepository){
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    @GetMapping
    public String index(Model model){
        //security context holdernya userdto -> use pricipal
        // String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = null;

        if (principal instanceof UserDTO) {
            UserDTO user = (UserDTO) principal;
            userId = user.getId();  
        }

        ProfileDTO profile = userRepository.getUsingProfileDTO(userId);

        //UserDTO user = userRepository.getUsingProfileDTO(email);  // This should be the correct one
        model.addAttribute("profile", profile);
        return "user/profile";
    }
}
