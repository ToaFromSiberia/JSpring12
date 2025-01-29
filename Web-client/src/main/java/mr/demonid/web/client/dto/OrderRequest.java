package mr.demonid.web.client.dto;

import lombok.Data;

import java.util.List;

/**
 * Запрос на покупку товаров.
 */
@Data
public class OrderRequest {
    private String userId;
    private String paymentMethod;
    private List<CartItem> cartItems;
}
