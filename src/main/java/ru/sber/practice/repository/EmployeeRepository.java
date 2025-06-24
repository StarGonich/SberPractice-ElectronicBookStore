package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
//    @Query(value = "select * from clients where nickname = :nickname", nativeQuery = true)
    Optional<Employee> findByNickname(String nickname);
//    @Query(value = "select * from clients where email = :email", nativeQuery = true)
    Optional<Employee> findByEmail(String email);
}
