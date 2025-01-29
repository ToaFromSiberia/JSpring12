package mr.demonid.service.cart.services;

import mr.demonid.service.cart.dto.CartItem;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Заглушка, на случай если пользователя нельзя опознать.
 */
@Component
public class EmptyCart implements Cart {
    @Override
    public CartItem addItem(String productId, int quantity) {
        return null;
    }

    @Override
    public void removeItem(String productId) {
    }

    @Override
    public List<CartItem> getItems() {
        return List.of();
    }

    @Override
    public int getQuantity() {
        return 0;
    }

    @Override
    public void clearCart() {

    }
}
