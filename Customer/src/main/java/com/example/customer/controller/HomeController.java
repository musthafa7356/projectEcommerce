package com.example.customer.controller;

import com.example.library.service.CustomerService;
import org.springframework.stereotype.Controller;
@Controller
public class HomeController {

    private final CustomerService customerService;

    public HomeController(CustomerService customerService){
        this.customerService = customerService;
    }
}
