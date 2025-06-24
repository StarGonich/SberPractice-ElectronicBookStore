package ru.sber.practice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.practice.config.ClientDetails;
import ru.sber.practice.config.EmployeeDetails;
import ru.sber.practice.model.Employee;
import ru.sber.practice.repository.EmployeeRepository;

@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee addEmployee(Employee employee) {
        if (employeeRepository.findByNickname(employee.getNickname()).isPresent()) {
            throw new IllegalStateException("Данный nickname уже занят");
        }

        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new IllegalStateException("Пользователь с таким email уже существует");
        }

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new EmployeeDetails(employeeRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "сотрудник не найден")));
    }
}
