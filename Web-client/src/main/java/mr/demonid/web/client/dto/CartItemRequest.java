package mr.demonid.web.client.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private String userId;
    private String productId;
    private int quantity;
}
