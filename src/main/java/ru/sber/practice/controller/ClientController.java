package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sber.practice.model.Client;
import ru.sber.practice.service.ClientService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new Client());
        return "register";
    }

    @PostMapping(path = "/register")
    public String registerClient(@ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        try {
            clientService.registerClient(client);
            redirectAttributes.addFlashAttribute("success", "Регистрация прошла успешно!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/register";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("admin/clients")
    public String findAllClients(Model model) {
        List<Client> clients = clientService.findAllClients();
        model.addAttribute("clients", clients);
        return "admin/clients";
    }
}
