package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.dto.LoginDTO;

@Controller
@RequestMapping("dashboard")
public class DashboardController{
    @GetMapping
    public String index(Model model) {
        model.addAttribute("dashboard", new LoginDTO());
        return "dashboard/index";
    }
}
