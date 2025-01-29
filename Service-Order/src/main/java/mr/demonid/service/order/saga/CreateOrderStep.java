package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderItem;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.repository.OrderRepository;
import mr.demonid.service.order.services.InformationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Шаг: создание заказа.
 */
@AllArgsConstructor
public class CreateOrderStep implements SagaStep<SagaContext> {

    private final OrderRepository orderRepository;
    private final InformationService informationService;

    @Override
    public void execute(SagaContext context) throws SagaStepException {
        Order order = Order.builder()
                .userId(context.getUserId())
                .totalPrice(context.getTotalAmount())
                .paymentMethod(context.getPaymentMethod())
                .status(OrderStatus.Pending)
                .build();

        Order finalOrder = order;
        List<OrderItem> orderItems = context.getItems().stream()
                .map(e -> OrderItem.builder()
                        .order(finalOrder) // Передаем ссылку на созданный заказ
                        .productId(e.getProductId())
                        .productName(e.getProductName())
                        .quantity(e.getQuantity())
                        .price(e.getPrice())
                        .build()
                ).toList();

        order.setItems(new ArrayList<>(orderItems));    // иначе получим неизменяемый список items'ов. Спасибо lombok, за прекрасные пару часов отладки! А надо то было, всего лишь документацию почитать на @Builder :)
        order = orderRepository.save(order);
        context.setOrderId(order.getOrderId());
        if (order.getOrderId() == null) {
            throw new SagaStepException("Ошибка создания заказа. Проблемы с БД");      // ошибка создания заказа, возможно БД недоступна
        }
    }

    @Override
    public void rollback(SagaContext context) {
        if (context.getOrderId() != null) {
            Order order = orderRepository.findById(context.getOrderId()).orElse(null);
            if (order != null) {
                // меняем статус заказа на "отменен"
                order.setStatus(OrderStatus.Cancelled);
                order = orderRepository.save(order);
            }
            context.setOrderId(null);
        }
        informationService.sendMessage("Заказ отменен. Попробуйте попозже.");
    }
}
