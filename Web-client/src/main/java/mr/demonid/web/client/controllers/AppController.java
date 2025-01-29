package mr.demonid.web.client.controllers;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.service.CartService;
import mr.demonid.web.client.service.CatalogService;
import mr.demonid.web.client.service.OrderService;
import mr.demonid.web.client.service.PaymentService;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;


@Controller
@AllArgsConstructor
public class AppController {

    private CatalogService catalogService;
    private CartService cartService;
    private PaymentService paymentService;
    private OrderService orderService;


    /**
     * Прием информационного сообщения от другого микросервиса.
     */
    @RabbitListener(queues = "springCloudBusQueuePK8000")
    public void handleBusMessage(String message) {
        System.out.println("---->>>> receive: " + message);
        // дальше можно отправить его пользователю, например на мыло,
        // или сделать на главной странице область для таких сообщений.
    }


    @GetMapping
    public String baseDir() {
        System.out.println("redirect to index...");
        return "redirect:/index";
    }

    /**
     * Отображение главной страницы магазина.
     * @param session Текущая сессия, для хранения настроек.
     */
    @GetMapping("/index")
    public String index(HttpSession session, Model model) {

        boolean isAuthenticated = IdnUtil.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? IdnUtil.getUserName() : null);

        List<ProductInfo> products;
        // Создаем список категорий продуктов.
        List<String> categories = catalogService.getCategories();
        categories.add("All");
        // Настраиваем страницу
        String category = (String) session.getAttribute("category");    // смотрим, есть ли в сессии данные о текущей категории?
        if (category != null && !category.equals("All")) {
            products = catalogService.getProductsByCategory(category);
        } else {
            products = catalogService.getProducts();
            category = "All";
        }
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", category);
        model.addAttribute("products", products);

        model.addAttribute("cartItemCount", cartService.getProductCount());
        return "/home";
    }


    /**
     * Фильтр категорий товара. Просто задает тип категории и уходит обратно на страницу.
     * @param session  Текущая сессия, где мы будем временно хранить тип категории.
     * @param category Выбранный пользователем тип категории товара.
     */
    @GetMapping("/set-category")
    public String setCategory(HttpSession session, Model model, @RequestParam("category") String category) {
        session.setAttribute("category", category);
        return "redirect:/index";
    }

    /**
     * Используется для перенаправления пользователя на форму авторизации.
     */
    @GetMapping("do-login")
    public String doLogin() {
        return "redirect:/index";
    }


    /**
     * Добавляем товар в корзину.
     */
    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity, HttpServletRequest request) {
        // Отправляем товар в корзину
        System.out.println("-->> user: " + IdnUtil.getAnonymousId(request));
        cartService.addToCart(productId.toString(), quantity);
        return "redirect:/index";
    }

    /**
     * Переход на страницу корзины товаров.
     * Только для авторизированных пользователей.
     * Хотя никто не мешает авторизировать пользователя и попозже, когда
     * нажмет кнопку оплаты. Но мне так захотелось.
     */
    @GetMapping("cart")
    public String placeOrder(Model model) {
        // задаем список товаров в корзине
        List<CartItem> items = cartService.getCartItems();
        model.addAttribute("cartItems", items);
        // задаем список возможных стратегий оплаты.
        List<StrategyInfo> payments = paymentService.getPaymentStrategies();
        model.addAttribute("paymentMethods", payments);
        return "cart";
    }

    /**
     * Формирование запроса на оплату товаров.
     */
    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("paymentMethod") String paymentMethod, Model model) {
        try {
            List<CartItem> items = cartService.getCartItems();
            if (items.isEmpty()) {
                model.addAttribute("errorMessage", "Корзина пуста");
                return "/error-order";
            }
            OrderRequest order = new OrderRequest();
            order.setUserId(IdnUtil.getUserId());
            order.setPaymentMethod(paymentMethod);
            order.setCartItems(items);
            System.out.println("-- order processed: " + order);
            orderService.createOrder(order);
            // показываем результат покупки
            List<String> names = items.stream().map(CartItem::getProductName).toList();
            List<Integer> quantity = items.stream().map(CartItem::getQuantity).toList();
            BigDecimal totalPrice = items.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("productName", names.toString());
            model.addAttribute("quantity", quantity);
            model.addAttribute("totalCost", totalPrice);
            System.out.println("-- покупка совершена!");
            return "/confirmed";

        } catch (FeignException e) {
            System.out.println("Облом!");
            model.addAttribute("errorMessage", e.contentUTF8());
            return "/error-order";
        }
    }

}

