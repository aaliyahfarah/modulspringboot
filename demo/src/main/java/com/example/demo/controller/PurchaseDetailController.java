package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("purchase-detail")
public class PurchaseDetailController {
    @GetMapping
    public String index(){
        return "purchase/index";
    }
}
