package mr.demonid.service.order.saga;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Контекст для передачи данных между шагами.
 */
@Data
public class SagaContext {
    private UUID orderId;
    private Long userId;
    private String paymentMethod;
    private BigDecimal totalAmount;

    private List<SagaContextItems> items;

}
