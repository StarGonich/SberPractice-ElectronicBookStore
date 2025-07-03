package ru.sber.practice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sber.practice.model.Book;
import ru.sber.practice.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookByID(int id) {
        return bookRepository.findById(id).get();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
}
