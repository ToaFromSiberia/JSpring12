package mr.demonid.service.cart.services;

import mr.demonid.service.cart.dto.CartItem;

import java.util.List;

public interface Cart {

    CartItem addItem(String productId, int quantity);
    void removeItem(String productId);
    List<CartItem> getItems();
    int getQuantity();
    void clearCart();
}
