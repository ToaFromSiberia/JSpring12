package mr.demonid.service.order.dto;

import lombok.Data;

import java.util.List;

/**
 * Запрос на покупку товаров.
 */
@Data
public class OrderRequest {
    private Long userId;
    private String paymentMethod;
    private List<CartItem> cartItems;
}
