package mr.demonid.service.payment.cor;


import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.PaymentEntity;
import mr.demonid.service.payment.domain.PaymentStatus;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Действия при неудавшейся операции.
 */
@Service
@AllArgsConstructor
public class RollbackTransfer {

    PaymentRepository paymentRepository;


    public void rollback(PaymentContext context) {
        System.out.println("RollbackTransfer()");
        try {
            // помечаем операция как неудавшуюся
            Optional<PaymentEntity> entity = paymentRepository.findByOrderId(context.getOrderId());
            if (entity.isPresent()) {
                entity.get().setStatus(PaymentStatus.Cancelled);
                paymentRepository.save(entity.get());
            }
        } catch (Exception ignored) {}
    }

}
