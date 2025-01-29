package mr.demonid.service.payment.dto;

import lombok.Data;
import mr.demonid.service.payment.services.strategy.PaymentStrategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Контекст данных платежа.
 */
@Data
public class PaymentContext {
    private UUID orderId;
    private Long userId;
    private BigDecimal amount;
    private PaymentStrategy paymentStrategy;


    public PaymentContext(UUID orderId, Long userId, BigDecimal amount, PaymentStrategy paymentStrategy) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentStrategy = paymentStrategy;
    }

}

