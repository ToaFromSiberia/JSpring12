package mr.demonid.web.client.links;

import jakarta.servlet.http.HttpServletRequest;
import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.CartItemRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Обращение к микросервису Cart-service.
 */
@FeignClient(name = "CART-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface CartServiceClient {

    @GetMapping("/api/cart/count")
    ResponseEntity<Integer> getItemQuantity();

    @PostMapping("/api/cart/add")
    ResponseEntity<CartItemRequest> addItem(@RequestParam String productId, @RequestParam Integer quantity);

    @GetMapping("/api/cart")
    ResponseEntity<List<CartItemRequest>> getItems();

    @GetMapping("/api/cart/clear")
    ResponseEntity<Void> clearCart();

    @PostMapping("/api/cart/register-user")
    ResponseEntity<Void> registerUser(@RequestParam String anonId, @RequestParam String userId);
}