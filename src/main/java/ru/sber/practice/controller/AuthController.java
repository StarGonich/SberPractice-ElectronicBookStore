package ru.sber.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String clientLogin() { return "login"; }

    @GetMapping("/admin/login")
    public String adminLogin() { return "admin/login"; }

    @GetMapping("/employee/login")
    public String employeeLogin() { return "employee/login"; }
}
