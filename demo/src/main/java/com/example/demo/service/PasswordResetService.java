package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;

@Service
public class PasswordResetService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SendEmailService sendEmailService;
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    public PasswordResetService(UserRepository userRepository, PasswordEncoder passwordEncoder,
    SendEmailService sendEmailService, PasswordResetTokenRepository tokenRepository
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sendEmailService = sendEmailService;
        this.tokenRepository = tokenRepository;
    }   

    //exp 30 menit
    private static final long EXPIRATION_TIME = 15;

    public boolean generateResetToken(String email) {
        User user = userRepository.findByEmail(email);  

        if (user != null) {
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(EXPIRATION_TIME));
            tokenRepository.save(passwordResetToken);

            String subject = "Permintaan Perubahan Password pada Akun Bliken " + user.getPerson().getFirstName();
            String body = "Jangan berikan link ini kepada siapapun!\n\n" +
                        "Untuk mengganti password, tekan link di bawah ini :\n" + 
                          "http://localhost:8080/user-management/reset-password?token=" + token;
                      
            if(sendEmailService.sendEmail(user.getPerson().getEmail(), subject, body)){
                return true;
            }else {return false;}
        } else {
            return false;
        }
    }

    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);

        if (resetTokenOpt.isPresent()) {
            PasswordResetToken resetToken = resetTokenOpt.get();
            if (resetToken.getExpirationDate().isAfter(LocalDateTime.now())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false; 
        }
    }

    public Boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);
        if (resetTokenOpt.isPresent()) {
            PasswordResetToken resetToken = resetTokenOpt.get();
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            String subject = "Perubahan Password pada Akun Bliken " + user.getPerson().getFirstName();
            String body = "Halo " + user.getPerson().getFirstName() + "!,\n\n" +
                        "Password anda telah diubah menjadi: " + newPassword + "\n\n" +
                        "Untuk alasan keaman, jangan berikan password ini kepada siapaun.\n\n" +
                        "Terima kasih";
            if(sendEmailService.sendEmail(user.getPerson().getEmail(), subject, body)){
                return true;
            }else {return false;}
        } else {
            return false;
        }
    }
}
