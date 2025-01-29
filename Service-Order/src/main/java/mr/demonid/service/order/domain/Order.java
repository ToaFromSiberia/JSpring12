package mr.demonid.service.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Таблица заказов.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String paymentMethod;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items;


    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }


    /**
     * Порождающий паттерн Builder.
     * И почему в Java нет #define? Сколько работы можно было сократить...
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Order order;

        public Builder() {
            order = new Order();
            order.status = OrderStatus.Pending;
            order.items = new ArrayList<>();
        }

        public Builder userId(long userId) {
            order.userId = userId;
            return this;
        }

        public Builder totalPrice(BigDecimal totalPrice) {
            // Конечно не помешает, но это все дальше проверяется, зря что-ли Saga использовал?
//            if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
//                throw new IllegalArgumentException("Стоимость не может быть меньше и равной нулю");
//            }
            order.totalPrice = totalPrice;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            // дальше проверится
//            if (paymentMethod == null || paymentMethod.isBlank()) {
//                throw new IllegalArgumentException("Неверный метод оплаты");
//            }
            order.paymentMethod = paymentMethod;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            order.createAt = createAt;
            return this;
        }

        public Builder createAtNow() {
            order.createAt = LocalDateTime.now();
            return this;
        }

        public Builder status(OrderStatus status) {
            order.status = status;
            return this;
        }

        public Order build() {
            if (order.createAt == null) {
                order.createAt = LocalDateTime.now();
            }
            return order;
        }
    }

}
