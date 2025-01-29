package mr.demonid.service.cart.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.service.cart.dto.CartItem;
import mr.demonid.service.cart.services.Cart;
import mr.demonid.service.cart.services.CartFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер корзины.
 * Данные по авторизированным пользователям извлекаются из токена Jwt.
 * Для анонимных пользователей используются куки. При первом обращении генерируется
 * уникальный UUID и сохраняется в cookie с именем anon_id, который используется при
 * каждом обращении к корзине, пока клиент не разорвет связь.
 */
@RestController
@Slf4j
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartFactory cartFactory;


    /**
     * Добавление товара в корзину.
     * @param productId Товар
     * @param quantity  Количество
     * @param request   Запрос, для идентификации пользователя.
     */
    @PostMapping("/add")
    public ResponseEntity<CartItem> addItem(@RequestParam String productId, @RequestParam Integer quantity, HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        return ResponseEntity.ok(cart.addItem(productId, quantity));
    }

    /**
     * Возвращает список товаров в корзине пользователя.
     */
    @GetMapping
    public ResponseEntity<List<CartItem>> getItems(HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        return ResponseEntity.ok(cart.getItems());
    }

    /**
     * Возвращает общее количество товаров в корзине.
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getItemQuantity(HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        return ResponseEntity.ok(cart.getQuantity());
    }

    /**
     * Очищает корзину.
     */
    @GetMapping("clear")
    public ResponseEntity<String> clearCart(HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        cart.clearCart();
        return ResponseEntity.ok().build();
    }

    /**
     * Регистрирует пользователя.
     * @param anonId Идентификатор пользователя до авторизации.
     * @param userId Текущий идентификатор пользователя.
     */
    @PostMapping("/register-user")
    public ResponseEntity<Void> registerUser(@RequestParam String anonId, @RequestParam String userId) {
        cartFactory.registerUser(anonId, userId);
        return ResponseEntity.ok().build();
    }

}
