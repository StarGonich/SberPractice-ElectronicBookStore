package ru.sber.practice.service;

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

    public Order addToCart(String nickname, int bookId, int count) {
        Optional<Client> client = clientRepository.findByNickname(nickname);
        if (client.isEmpty()) {
            throw new IllegalStateException("Клиент с таким Nickname не существует");
        }

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new IllegalStateException("Книга с таким ID не существует");
        }

        Optional<Order> existingOrder = orderRepository.findInCartByClientAndBook(client.get().getId(), bookId);

        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            int newCount = order.getCount() + count;
            System.out.print("newCount =");
            System.out.println(newCount);
            System.out.print("book.get().getStockQuantity() =");
            System.out.println(book.get().getStockQuantity());
            if (newCount > book.get().getStockQuantity()) {
                throw new IllegalStateException("Запрашиваемое количество превышает доступный запас книги");
            }
            order.setCount(newCount);
            orderRepository.save(order);
            return order;
        } else {
            System.out.print("count =");
            System.out.println(count);
            System.out.print("book.get().getStockQuantity() =");
            System.out.println(book.get().getStockQuantity());
            if (count > book.get().getStockQuantity()) {
                throw new IllegalStateException("Запрашиваемое количество превышает доступный запас книги");
            }
            Order order = new Order();
            order.setClient(client.get());
            order.setBook(book.get());
            order.setCount(count);
            order.setStatus(OrderStatus.in_cart);
            orderRepository.save(order);
            return order;
        }
    }

    public List<Order> findAllOrdersInCart(String nickname) {
        Optional<Client> client = clientRepository.findByNickname(nickname);
        if (client.isEmpty()) {
            throw new IllegalStateException("Клиент с таким Nickname не существует");
        }
        return orderRepository.findAllInCart(client.get().getId());
    }

    public List<Order> findAllOrdersShipped(String nickname) {
        Optional<Client> client = clientRepository.findByNickname(nickname);
        if (client.isEmpty()) {
            throw new IllegalStateException("Клиент с таким Nickname не существует");
        }
        return orderRepository.findAllShipped(client.get().getId());
    }

    public BigDecimal calculateTotalPrice(List<Order> cartItems) {
        return cartItems.stream()
                .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getStockQuantity(Order order) {
        Book book = bookRepository.findById(order.getBook().getId()).get();
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
}
