package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.CartItemRequest;
import mr.demonid.web.client.dto.ProductInfo;
import mr.demonid.web.client.links.CartServiceClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    CartServiceClient cartServiceClient;
    CatalogService catalogService;

    public Integer getProductCount() {
        try {
            return cartServiceClient.getItemQuantity().getBody();
        } catch (FeignException e) {
            System.out.println("getProductCount: " + e.contentUTF8());
            return 0;
        }
    }

    public void addToCart(String productId, Integer quantity) {
        try {
            cartServiceClient.addItem(productId, quantity);
        } catch (FeignException e) {
            System.out.println("addToCart: " + e.contentUTF8());
        }
    }

    /**
     * Возвращает содержимое корзины текущего пользователя.
     */
    public List<CartItem> getCartItems() {
        List<CartItem> items = Collections.emptyList();

        List<CartItemRequest> requests = cartServiceClient.getItems().getBody();
        if (requests != null && !requests.isEmpty()) {
            items = requests.stream().map(e -> {
                ProductInfo product = catalogService.getProductById(Long.parseLong(e.getProductId()));
                return new CartItem(
                        e.getProductId(),
                        product.getName(),
                        e.getQuantity(),
                        product.getPrice(),
                        product.getPrice().multiply(new BigDecimal(e.getQuantity()))
                );
            }).toList();
        }
        return items;
    }

    public void transferAnonToUser(String anonId, String userId) {
        System.out.println("transfer cart from '" + anonId + "' to '" + userId + "'");
        cartServiceClient.registerUser(anonId, userId);
    }

}
