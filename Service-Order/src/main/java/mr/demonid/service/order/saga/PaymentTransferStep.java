package mr.demonid.service.order.saga;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.service.order.dto.PaymentRequest;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.links.PaymentServiceClient;
import mr.demonid.service.order.services.InformationService;

import java.math.BigDecimal;

/**
 * Шаг: Оплата заказа.
 */
@AllArgsConstructor
public class PaymentTransferStep implements SagaStep<SagaContext> {

    private final PaymentServiceClient paymentServiceClient;


    @Override
    public void execute(SagaContext context) throws SagaStepException {
        try {
            PaymentRequest paymentRequest = new PaymentRequest(
                    context.getOrderId(),
                    context.getUserId(),
                    context.getTotalAmount(),
                    context.getPaymentMethod(),
                    "BUY");
            paymentServiceClient.transfer(paymentRequest);
        } catch (FeignException e) {
            throw new SagaStepException(e.contentUTF8());
        }
    }

    @Override
    public void rollback(SagaContext context) {
        try {
            paymentServiceClient.rollback(context.getOrderId());
        } catch (FeignException ignored) {}
    }

}
