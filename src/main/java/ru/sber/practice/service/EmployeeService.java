package ru.sber.practice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.practice.config.ClientDetails;
import ru.sber.practice.config.EmployeeDetails;
import ru.sber.practice.model.Employee;
import ru.sber.practice.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void addEmployee(Employee employee) {
        if (employeeRepository.findByNickname(employee.getNickname()).isPresent()) {
            throw new IllegalStateException("Данный nickname уже занят");
        }

        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new IllegalStateException("Пользователь с таким email уже существует");
        }

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }

    public void editEmployee(Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(updatedEmployee.getId())
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с id='" + updatedEmployee.getId() + "' не найден"));

        if (!existingEmployee.getNickname().equals(updatedEmployee.getNickname())) {
            if (employeeRepository.findByNickname(updatedEmployee.getNickname()).isPresent()) {
                throw new IllegalStateException("Данный nickname уже занят");
            }
            existingEmployee.setNickname(updatedEmployee.getNickname());
        }

        if (!existingEmployee.getEmail().equals(updatedEmployee.getEmail())) {
            if (employeeRepository.findByEmail(updatedEmployee.getEmail()).isPresent()) {
                throw new IllegalStateException("Пользователь с таким email уже существует");
            }
            existingEmployee.setEmail(updatedEmployee.getEmail());
        }

        // Обновляем пароль только если он был изменен
        if (updatedEmployee.getPassword() != null && !updatedEmployee.getPassword().isEmpty()) {
            existingEmployee.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
        }

        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(int id) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с id='" + id + "' не найден"));
        employeeRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new EmployeeDetails(employeeRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "сотрудник не найден")));
    }
}
