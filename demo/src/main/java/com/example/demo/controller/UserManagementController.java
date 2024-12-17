package com.example.demo.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.Person;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.dto.ForgetPasswordDTO;
import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.RegisterDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PasswordResetService;
import com.example.demo.service.SendEmailService;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.PersonRepository;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("user-management")
public class UserManagementController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PersonRepository personRepository;
    private PasswordEncoder passwordEncoder;
    private SendEmailService sendEmailService;
    private PasswordResetService passwordResetService;

    @Autowired
    public UserManagementController(UserRepository userRepository, PasswordEncoder passwordEncoder, 
    RoleRepository roleRepository, PersonRepository personRepository, SendEmailService sendEmailService,
    PasswordResetService passwordResetService
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.sendEmailService = sendEmailService;
        this.passwordResetService = passwordResetService;
    }   

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "user/register";
    }

    @PostMapping("add")
    public String add(RegisterDTO registerDTO) {
        if (!personRepository.existsByTelephone(registerDTO.getTelephone())) {
            if (!personRepository.existsByEmail(registerDTO.getEmail())) {
                Person person = new Person();
                person.setFirstName(registerDTO.getFirstName());
                person.setLastName(registerDTO.getLastName());
                person.setTelephone(registerDTO.getTelephone());
                person.setEmail(registerDTO.getEmail());
                person.setAddress(registerDTO.getAddress());
                person = personRepository.save(person);

                Role defaultRole = roleRepository.findByName("customer");

                User user = new User();
                user.setPerson(person);
                user.setRole(defaultRole);
                user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                userRepository.save(user);

                String subject = "Selamat datang di Bliken: Platform Jual Beli Kendaraan";
                String body = "Hi " + person.getFirstName() + "!," 
                + "\n\nSelamat bergabung di Bliken!\n\n";
                sendEmailService.sendEmail(person.getEmail(), subject, body);
            }
        }
        return "redirect:/user-management/login";
    }

    // loginnn
    @GetMapping("login")
    public String index(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "user/login";
    }

    @PostMapping("authenticate")
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
        
        return "redirect:/user-management/login";
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(String role){
        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    // forget password
    @GetMapping("forget-password")
    public String forgetPassword(Model model) {
        model.addAttribute("forgetPasswordDTO", new ForgetPasswordDTO()); 
        return "user/forgetPassword";  
    }

    @PostMapping("forget-password")
    public String handleForgetPassword(@ModelAttribute ForgetPasswordDTO forgetPasswordDTO) {
        passwordResetService.generateResetToken(forgetPasswordDTO.getEmail());
        return "user/forgetPasswordSuccess"; 
    }

    @GetMapping("reset-password")
    public String resetPassword(@RequestParam("token") String token, Model model) {
        if (passwordResetService.validateResetToken(token)) {
            model.addAttribute("token", token);
            return "user/resetPassword"; 
        } else {
            model.addAttribute("message", "Invalid or expired token.");
            return "user/message";
        }
    }

    @PostMapping("reset-password")
    public String handleResetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword, Model model) {
        System.out.println("Received token: " + token);
        passwordResetService.resetPassword(token, newPassword);

        model.addAttribute("message", "Password berhasil diubah");

        return "user/message";  
    }
}
