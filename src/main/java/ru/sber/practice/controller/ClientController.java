package ru.sber.practice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.sber.practice.model.Client;
import ru.sber.practice.service.ClientService;

import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new Client());
        return "register";
    }

//    @PostMapping(path = "/register")
//    public Client registerClient(@RequestBody Client client) {
//        return clientService.registerClient(client);
//    }

    @PostMapping(path = "/register")
    public String registerClient(@ModelAttribute Client client, Model model) {
        try {
            clientService.registerClient(client);
            model.addAttribute("success", "Регистрация прошла успешно!");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "register";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("admin/clients")
    public String findAllClients(Model model) {
        List<Client> clients = clientService.findAllClients();
        model.addAttribute("clients", clients);
        return "admin/clients";
    }
}
