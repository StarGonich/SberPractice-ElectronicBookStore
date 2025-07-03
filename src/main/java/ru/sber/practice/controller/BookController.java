package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sber.practice.model.Book;
import ru.sber.practice.service.BookService;
import ru.sber.practice.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final OrderService orderService;

    @GetMapping
    public String showHomePage(Model model) {
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "home";
    }

    @GetMapping("/book")
    public String showBookDetails(@RequestParam(name = "id") int bookId, Model model,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookService.findBookByID(bookId);
        model.addAttribute("book", book);
        if (userDetails != null &&  userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CLIENT"))) {
            String nickname = userDetails.getUsername();
            try {
                int quantityInCart = orderService.getBookQuantityInCart(nickname, bookId);
                model.addAttribute("quantityInCart", quantityInCart);
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
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
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookService.addBook(book);
            redirectAttributes.addFlashAttribute("success", "Книга успешно добавлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employee/add-book";
    }
}
