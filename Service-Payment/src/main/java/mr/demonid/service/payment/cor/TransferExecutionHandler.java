package mr.demonid.service.payment.cor;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.dto.PaymentContext;

/**
 * Шаг: собственно списание средств, через выбранную систему оплаты.
 */
@AllArgsConstructor
public class TransferExecutionHandler extends PaymentHandler {

    @Override
    protected void handle(PaymentContext context) {
        System.out.println("-- TransferExecutionHandler()");
        context.getPaymentStrategy().pay(context.getUserId(), context.getAmount());
    }

}
