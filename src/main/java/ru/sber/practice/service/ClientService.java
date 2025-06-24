package ru.sber.practice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.practice.config.ClientDetails;
import ru.sber.practice.model.Client;
import ru.sber.practice.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client registerClient(Client client) {
        if (clientRepository.findByNickname(client.getNickname()).isPresent()) {
            throw new IllegalStateException("Данный nickname уже занят");
        }

        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new IllegalStateException("Пользователь с таким email уже существует");
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new ClientDetails(clientRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "клиент не найден")));
    }
}
