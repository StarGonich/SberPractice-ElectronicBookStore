package ru.sber.practice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sber.practice.model.Client;
import ru.sber.practice.model.Order;
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
    public String addBookToCart(Principal principal,
                                @RequestParam("bookId") int bookId, @RequestParam("quantity") int count,
                                RedirectAttributes redirectAttributes) {
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

        Client client = orderService.getClient(nickname);
        List<Order> cartItems = orderService.findAllOrdersInCart(client);
        BigDecimal totalPrice = orderService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("phone", client.getPhone());
        model.addAttribute("address", client.getAddress());
        return "cart";
    }

    @PostMapping("/cart/payment")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public String paymentOrder(Principal principal,
                               @RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "address", required = false) String address,
                               RedirectAttributes redirectAttributes) {
        String nickname = principal.getName();

        Client client = orderService.getClient(nickname);
        List<Order> cartItems = orderService.findAllOrdersInCart(client);

        if (cartItems == null || cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ваша корзина пуста");
            return "redirect:/cart";
        }

        // Проверяем, нужно ли обновлять контактные данные
        boolean needUpdate = false;
        if ((phone != null && !phone.isEmpty() && client.getPhone() == null) ||
                (address != null && !address.isEmpty() && client.getAddress() == null)) {
            needUpdate = true;
        }

        if (needUpdate && (phone == null || address == null)) {
            redirectAttributes.addFlashAttribute("error",
                    "Пожалуйста, заполните всю информацию для отправки");
            return "redirect:/cart";
        }

        try {
            if (needUpdate) {
                orderService.updateClientDeliveryInfo(client, phone, address);
            }
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

    @PostMapping("/cart/remove")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public String removeFromCart(@RequestParam int id) {
        orderService.deleteOrder(id);
        return "redirect:/cart";
    }
}
