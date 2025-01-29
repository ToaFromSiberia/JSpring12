package mr.demonid.service.payment.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.cor.PaymentHandler;
import mr.demonid.service.payment.cor.RollbackTransfer;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PaymentManager {

    private PaymentHandler paymentHandler;
    private RollbackTransfer rollbackTransfer;

    public void excecute(PaymentContext context) throws PaymentStepException {
        try {
            paymentHandler.run(context);

        } catch (PaymentStepException e) {
            System.out.println("-- Error: " + e.getMessage());
            rollbackTransfer.rollback(context);
            throw e;
        }
    }

}
