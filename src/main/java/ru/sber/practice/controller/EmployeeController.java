package ru.sber.practice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sber.practice.model.Client;
import ru.sber.practice.model.Employee;
import ru.sber.practice.service.EmployeeService;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("admin/employees")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/add-employee";
    }

    @PostMapping("admin/employees")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addEmployee(@ModelAttribute Employee employee, Model model) {
        try {
            employeeService.addEmployee(employee);
            model.addAttribute("success", "Добавление произошло успешно!");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/add-employee";
    }
}
