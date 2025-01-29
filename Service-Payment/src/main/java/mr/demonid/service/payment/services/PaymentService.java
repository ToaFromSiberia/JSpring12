package mr.demonid.service.payment.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.dto.PaymentRequest;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.services.strategy.PaymentStrategyRegistry;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PaymentService {

    PaymentStrategyRegistry paymentStrategyRegistry;
//    PaymentRepository paymentRepository;
//    UserRepository userRepository;

    PaymentManager paymentManager;

    /**
     * Списание средств со счета пользователя в пользу магазина.
     */
    public Boolean transfer(PaymentRequest request) {
        System.out.println("-- transfer method called");
        // создаем контекст данных
        PaymentContext context = new PaymentContext(
                request.getOrderId(),
                request.getUserId(),
                request.getAmount(),
                paymentStrategyRegistry.getStrategy(request.getPaymentMethod())
        );
        System.out.println("  -- transfer context: " + context);
        paymentManager.excecute(context);
//        // настраиваем последовательность действий
//        OrchestratorPayment<PaymentContext> orchestrator = new OrchestratorPayment<>();
//        orchestrator.addStep(new CreateTransferStep(paymentRepository));
//        orchestrator.addStep(new CheckBalanceStep(paymentRepository, userRepository));
//        orchestrator.addStep(new CheckMethodStep());
//        orchestrator.addStep(new PaymentExecutionStep());
//        orchestrator.addStep(new PaymentApprovedStep(paymentRepository));
//        // выполняем
//        orchestrator.execute(context);
        return true;
    }


}
