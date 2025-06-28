package ru.sber.practice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("employee", new Employee());
        return "admin/employees";
    }

    @PostMapping("admin/employees")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        try {
            employeeService.addEmployee(employee);
            redirectAttributes.addFlashAttribute("success", "Добавление произошло успешно!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/employees";
    }

    @PostMapping("admin/employee/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteEmployee(@RequestParam int id) {
        employeeService.deleteEmployee(id);
        return "redirect:/admin/employees";
    }

}
