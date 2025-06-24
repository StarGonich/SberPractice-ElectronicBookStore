package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query(value = "select * from clients where nickname = :nickname", nativeQuery = true)
    Optional<Client> findByNickname(String nickname);

    @Query(value = "select * from clients where email = :email", nativeQuery = true)
    Optional<Client> findByEmail(String email);
}
