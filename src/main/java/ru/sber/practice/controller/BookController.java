package ru.sber.practice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sber.practice.model.Book;
import ru.sber.practice.service.BookService;

import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String showHomePage(Model model) {
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "home";
    }

    @GetMapping("/book")
    public String showBookDetails(@RequestParam Integer id, Model model) {
        Book book = bookService.findBookByID(id);
        model.addAttribute("book", book);
        return "book";
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @GetMapping("employee/add-book")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "employee/add-book";
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @PostMapping("employee/add-book")
    public String addBook(@ModelAttribute Book book, Model model) {
        try {
            bookService.addBook(book);
            model.addAttribute("success", "Книга успешно добавлена");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "employee/add-book";
    }
}
