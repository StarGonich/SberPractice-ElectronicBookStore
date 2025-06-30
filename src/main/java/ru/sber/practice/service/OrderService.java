package ru.sber.practice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.sber.practice.model.Order;
import ru.sber.practice.model.Client;
import ru.sber.practice.model.Book;
import ru.sber.practice.model.OrderStatus;
import ru.sber.practice.repository.BookRepository;
import ru.sber.practice.repository.ClientRepository;
import ru.sber.practice.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final BookRepository bookRepository;


    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.bookRepository = bookRepository;
    }

    public Client getClient(String nickname) {
        return clientRepository.findByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("Клиент с nickname='" + nickname + "' не найден"));
    }

    public Book getBook(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id='" + id + "' не найден"));
    }

    public void addToCart(String nickname, int bookId, int count) {
        Client client = getClient(nickname);
        Book book = getBook(bookId);

        Optional<Order> existingOrder = orderRepository.findInCartByClientAndBook(client.getId(), bookId);

        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            int newCount = order.getCount() + count;
            if (newCount > book.getStockQuantity()) {
                throw new IllegalStateException("Запрашиваемое количество превышает доступный запас книги");
            }
            order.setCount(newCount);
            orderRepository.save(order);
        } else {
            if (count > book.getStockQuantity()) {
                throw new IllegalStateException("Запрашиваемое количество превышает доступный запас книги");
            }
            Order order = new Order();
            order.setClient(client);
            order.setBook(book);
            order.setCount(count);
            order.setStatus(OrderStatus.in_cart);
            orderRepository.save(order);
        }
    }

    public List<Order> findAllOrdersInCart(Client client) {
        return orderRepository.findAllInCart(client.getId());
    }

    public List<Order> findAllOrdersShipped(String nickname) {
        Client client = getClient(nickname);
        return orderRepository.findAllShipped(client.getId());
    }

    public BigDecimal calculateTotalPrice(List<Order> cartItems) {
        return cartItems.stream()
                .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateClientDeliveryInfo(Client client, String phone, String address) {
        client.setPhone(phone);
        client.setAddress(address);
        clientRepository.save(client);
    }

    // Такая проверка позволяет узнать количество книг на самый последний момент
    public int getStockQuantity(Order order) {
        Book book = getBook(order.getBook().getId()); //Почему так, написано выше
        return book.getStockQuantity();
    }

    @Transactional
    public void payment(List<Order> cartItems) {
        for (Order item : cartItems) {
            int stockQuantity = getStockQuantity(item);
            int count = item.getCount();
            if (count > stockQuantity) {
                throw new IllegalStateException("Книги '" + item.getBook().getTitle() +
                        "' недостаточно на складе. Доступно: " + stockQuantity);
//                continue;
            }
            Book book = item.getBook();
            book.setStockQuantity(stockQuantity - count);
            bookRepository.save(book);
            item.setStatus(OrderStatus.shipped);
        }
    }

    public void deleteOrder(int id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с id='" + id + "' не найден"));
        orderRepository.deleteById(id);
    }

    public int getBookQuantityInCart(String nickname, int bookId) {
        Client client = getClient(nickname);
        Optional<Order> existingOrder = orderRepository.findInCartByClientAndBook(client.getId(), bookId);
        int count = 0;
        if (existingOrder.isPresent()) {
            count = existingOrder.get().getCount();
        }
        return count;
    }
}
