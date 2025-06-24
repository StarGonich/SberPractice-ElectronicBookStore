package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "select * from orders where client_id = :clientId and status = 'in_cart'", nativeQuery = true)
    List<Order> findAllInCart(int clientId);

    @Query(value = "select * from orders where client_id = :clientId and status = 'shipped'", nativeQuery = true)
    List<Order> findAllShipped(int clientId);

    @Query(value = "select * from orders where client_id = :clientId and book_id = :bookId and status = 'in_cart'",
            nativeQuery = true)
    Optional<Order> findInCartByClientAndBook(int clientId, int bookId);
}
