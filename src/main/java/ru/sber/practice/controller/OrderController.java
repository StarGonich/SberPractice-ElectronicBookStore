package ru.sber.practice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sber.practice.model.Book;
import ru.sber.practice.model.Client;
import ru.sber.practice.model.Order;
import ru.sber.practice.model.OrderStatus;
import ru.sber.practice.service.OrderService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("order/add")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public String addBookToCart(Principal principal, @RequestParam("bookId") int bookId, @RequestParam("quantity") int count, RedirectAttributes redirectAttributes) {
        try {
            String nickname = principal.getName();
            orderService.addToCart(nickname, bookId, count);
            redirectAttributes.addFlashAttribute("success", "Книга успешно добавлена в корзину");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/book?id=" + bookId;
    }

    @GetMapping("cart")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public String showCart(Principal principal, Model model) {
        String nickname = principal.getName();
        List<Order> cartItems = orderService.findAllOrdersInCart(nickname);
        BigDecimal totalPrice = orderService.calculateTotalPrice(cartItems);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping("/cart/payment")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public String paymentOrder(Principal principal, RedirectAttributes redirectAttributes) {
        String nickname = principal.getName();
        List<Order> cartItems = orderService.findAllOrdersInCart(nickname);

        if (cartItems == null || cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ваша корзина пуста");
            return "redirect:/cart";
        }

        try {
            orderService.payment(cartItems);
            redirectAttributes.addFlashAttribute("success", "Оплата произошло успешно");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/shopping-history")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public String shoppingHistory(Principal principal, Model model) {
        String nickname = principal.getName();
        model.addAttribute("orders", orderService.findAllOrdersShipped(nickname));
        return "shopping-history";
    }
}
