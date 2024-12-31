package com.example.demo.controller.api;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.Person;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.dto.ForgetPasswordDTO;
import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.RegisterDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PasswordResetService;
import com.example.demo.utils.CustomResponse;
import com.example.demo.utils.SendEmailService;

@RestController
@RequestMapping("api")
public class UserRestController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PersonRepository personRepository;
    private PasswordEncoder passwordEncoder;
    private SendEmailService sendEmailService;
    private PasswordResetService passwordResetService;
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    public UserRestController(UserRepository userRepository, PasswordEncoder passwordEncoder, 
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

    @GetMapping("user")
    public ResponseEntity<Object> get() {
        return CustomResponse.generate(HttpStatus.OK, "data berhasil didapatkan", userRepository.findAll());
    }

    // register
    @PostMapping("user/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        boolean emailHandling = false;
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

                // Send welcome email
                String subject = "Selamat datang di Bliken " + person.getFirstName();
                String body = "Hi " + person.getFirstName() + person.getLastName() + "!\n\nSelamat bergabung di Bliken!\n\n";
                emailHandling = sendEmailService.sendEmail(person.getEmail(), subject, body);

                if(emailHandling){
                    return CustomResponse.generate(HttpStatus.OK, "Register berhasil!", user);
                }else{
                    return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Email tidak terkirim");
                }      
            }
            return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Register gagal! Email atau telepon sudah terdaftar");
        }
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Register gagal! Email atau telepon sudah terdaftar");
    }

    // login
    @PostMapping("user/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
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

                return CustomResponse.generate(HttpStatus.OK, "Login berhasil!", user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return CustomResponse.generate(HttpStatus.OK, "Login gagal! Email atau password anda salah");
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(String role){
        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    //forget password
    // http://localhost:8080/api/user/forget-password
    @PostMapping("user/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        if(passwordResetService.generateResetToken(forgetPasswordDTO.getEmail())){
            return CustomResponse.generate(HttpStatus.OK, "Forget password berhasil! Silahkan cek email anda");
        }
        else{
            return CustomResponse.generate(HttpStatus.OK, "Email tidak ditemukan");
        }
    }

    //reset password
    //http://localhost:8080/api/user/reset-password?token=
    @PostMapping("user/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword, Model model) {
        if(passwordResetService.resetPassword(token, newPassword)){
            Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);
            PasswordResetToken resetToken = resetTokenOpt.get();
            tokenRepository.delete(resetToken);

            return CustomResponse.generate(HttpStatus.OK, "Reset password berhasil! Silahkan login");
        }else{
            return CustomResponse.generate(HttpStatus.NOT_FOUND, "Reset password gagal! Token invalid");
        } 
    }

    //change password
    @PostMapping("user/change-password")
    public ResponseEntity<?> changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = null;
        boolean emailHandling = false;

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
                emailHandling = sendEmailService.sendEmail(user.getPerson().getEmail(), subject, body);
  
                if(emailHandling){
                    return CustomResponse.generate(HttpStatus.OK, "Pergantian password berhasil! Email terkirim", user);
                }else{
                    return CustomResponse.generate(HttpStatus.OK, "Email tidak terkirim");
                } 
            } 
            return CustomResponse.generate(HttpStatus.OK, "Pergantian password gagal! Password salah");
        }
        return CustomResponse.generate(HttpStatus.OK, "Pergantian password gagal! Silahkan login dahulu");
    }
}
