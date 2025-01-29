package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SagaContextItems {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
