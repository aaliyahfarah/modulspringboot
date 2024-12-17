package com.example.demo.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
import com.example.demo.repository.PasswordResetTokenRepository;
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
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    public UserManagementController(UserRepository userRepository, PasswordEncoder passwordEncoder, 
    RoleRepository roleRepository, PersonRepository personRepository, SendEmailService sendEmailService,
    PasswordResetService passwordResetService, PasswordResetTokenRepository tokenRepository
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.sendEmailService = sendEmailService;
        this.passwordResetService = passwordResetService;
        this.tokenRepository = tokenRepository;
    }   

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "user/register";
    }

    @PostMapping("add")
    public String add(RegisterDTO registerDTO, Model model) {
        Boolean emailHandling = false;
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

                //tambahin timestamp/penomoran email
                String subject = "Selamat datang di Bliken " +person.getFirstName();
                String body = "Hi " + person.getFirstName() + "!," 
                + "\n\nSelamat bergabung di Bliken!\n\n";
                emailHandling = sendEmailService.sendEmail(person.getEmail(), subject, body);
            }
        }
        if(emailHandling){
            model.addAttribute("message", "Register berhasil!");
            model.addAttribute("buttonLink", "/user-management/login/");
            model.addAttribute("buttonText", "Kembali ke Halaman Login");
            return "user/message";    
        }else{
            model.addAttribute("message", "Register gagal!");
            model.addAttribute("buttonLink", "/user-management/register/");
            model.addAttribute("buttonText", "Kembali ke Halaman Register");  
            return "user/message";  
        }
    }

    // loginnn
    @GetMapping("login")
    public String index(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "user/login";
    }

    @PostMapping("authenticate")
    public String authenticate(LoginDTO login, Model model) {
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
        model.addAttribute("message", "Email / Password anda salah");
        model.addAttribute("buttonLink", "/user-management/login/");
        model.addAttribute("buttonText", "Kembali ke Halaman Login");
        return "user/message";
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
    public String handleForgetPassword(@ModelAttribute ForgetPasswordDTO forgetPasswordDTO, Model model) {
        if(passwordResetService.generateResetToken(forgetPasswordDTO.getEmail())){
            model.addAttribute("message", "Silahkan cek email anda untuk mereset password");
            model.addAttribute("buttonLink", "/user-management/login/");
            model.addAttribute("buttonText", "Kembali ke Halaman Login"); 
            return "user/message";
        }
        else{
            model.addAttribute("message", "Email tidak dapat ditemukan");
            model.addAttribute("buttonLink", "/user-management/forget-password/");
            model.addAttribute("buttonText", "Kembali"); 
            return "user/message";
        }
    }

    @GetMapping("reset-password")
    public String resetPassword(@RequestParam("token") String token, Model model) {
        if (passwordResetService.validateResetToken(token)) {
            model.addAttribute("token", token);
            return "user/resetPassword"; 
        } else {
            model.addAttribute("message", "Token Invalid atau Expired");
            model.addAttribute("buttonLink", "/user-management/login/");
            model.addAttribute("buttonText", "Kembali ke Halaman Login"); 
            return "user/message";
        }
    }

    @PostMapping("reset-password")
    public String handleResetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword, Model model) {
        if(passwordResetService.resetPassword(token, newPassword)){
            Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);
            PasswordResetToken resetToken = resetTokenOpt.get();
            tokenRepository.delete(resetToken);

            model.addAttribute("message", "Password berhasil diubah");
            model.addAttribute("buttonLink", "/user-management/login/");
            model.addAttribute("buttonText", "Kembali ke Halaman Login");
            return "user/message";
        }else{
            model.addAttribute("message", "Password gagal diubah");
            model.addAttribute("buttonLink", "/user-management/login/");
            model.addAttribute("buttonText", "Kembali ke Halaman Login");
            return "user/message";
        } 
    }

    @GetMapping("change-password")
    public String showChangePasswordForm(Model model) {
        return "user/changePassword";
    }

    @PostMapping("change-password")
    public String handleChangePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = null;

        if (principal instanceof UserDTO) {
            UserDTO user = (UserDTO) principal;
            userId = user.getId();
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                String subject = "Perubahan Password pada Akun Bliken " + user.getPerson().getFirstName();
                String body = "Halo " + user.getPerson().getFirstName() + "!,\n\n" +
                        "Password anda telah diubah menjadi:\n\n" +
                        "New Password: " + newPassword + "\n\n" +
                        "Untuk alasan keamanan, jangan berikan password ini kepada siapaun.\n\n" +
                        "Terima kasih";
                sendEmailService.sendEmail(user.getPerson().getEmail(), subject, body);
  
                    model.addAttribute("message", "Password berhasil diubah, silahkan login kembali");
                    model.addAttribute("buttonLink", "/user-management/login/");
                    model.addAttribute("buttonText", "Halaman login");
                    return "user/message";
            } 
        }

        model.addAttribute("message", "Password salah");
        model.addAttribute("buttonLink", "/user-management/forget-password/");
        model.addAttribute("buttonText", "Forget password");
        return "user/message";  
    }
    
}
