package mr.demonid.service.payment.cor;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.PaymentEntity;
import mr.demonid.service.payment.domain.PaymentStatus;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.repository.PaymentRepository;

import java.util.Optional;

/**
 * Шаг: завершение платежа, просто меняем статус на завершенный.
 */
@AllArgsConstructor
public class ApprovedTransferHandler extends PaymentHandler {

    PaymentRepository paymentRepository;


    @Override
    public void handle(PaymentContext context) {
        System.out.println("-- ApprovedTransferHandler()");
        Optional<PaymentEntity> entity = paymentRepository.findByOrderId(context.getOrderId());
        if (entity.isPresent()) {
            entity.get().setStatus(PaymentStatus.Approved);
            paymentRepository.save(entity.get());
            System.out.println("  -- approved done!");
        }
    }
}
