package com.example.demo.config;

import javax.management.RuntimeErrorException;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
    throws Exception{
            return authenticationConfiguration.getAuthenticationManager();
        }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    try{
                        auth
                                // .antMatchers("/").permitAll() landing page
                                .antMatchers("/category/**").hasAuthority("master")
                                .antMatchers("/user/**").hasRole("user")
                                .antMatchers("/role/**").hasAnyAuthority("master")
                                .antMatchers("admin/**", "/type/**", "/dashboard/**").authenticated()
                                .antMatchers("/user-management/login").permitAll()
                                .and()
                                .formLogin()
                                .loginPage("/user-management/login")
                                .and()
                                .httpBasic()
                                .and()
                                .logout()
                                .logoutUrl("/user/logout")
                                .logoutSuccessUrl("/user/login")
                                .permitAll();
                    } catch (Exception e) {
                        // throw new RuntimeErrorException(e);
                    }
                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public CorsFilter corsFilter(){

    // }
}
