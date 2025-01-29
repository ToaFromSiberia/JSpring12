package mr.demonid.service.payment.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.services.strategy.PaymentStrategyRegistry;
import mr.demonid.service.payment.dto.PaymentRequest;
import mr.demonid.service.payment.dto.StrategyInfo;
import mr.demonid.service.payment.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentStrategyRegistry strategyRegistry;

    /**
     * Списание зарезервированных средств.
     */
    @PostMapping("/transfer")
    public ResponseEntity<Boolean> transfer(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.transfer(request));
    }

    /**
     * Отмена резервирования средств и их возврат на счет пользователя.
     * @param orderId  Идентификатор заказа.
     * @return
     */
    @PostMapping("/rollback")
    ResponseEntity<Void> rollback(@RequestBody UUID orderId) {
        // В данной реализации не имеет смысла
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает список всех доступных способов оплаты.
     */
    @GetMapping("/strategies")
    public ResponseEntity<List<StrategyInfo>> getAvailableStrategies() {
        return ResponseEntity.ok(strategyRegistry.getAvailableStrategies());
    }
}
