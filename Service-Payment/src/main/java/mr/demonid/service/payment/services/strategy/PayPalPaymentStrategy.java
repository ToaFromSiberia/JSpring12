package mr.demonid.service.payment.services.strategy;

import mr.demonid.service.payment.exceptions.PaymentStepException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PayPalPaymentStrategy implements PaymentStrategy {

    @Override
    public void pay(Long userId, BigDecimal amount)  throws PaymentStepException {
        System.out.println("Using PayPalPaymentStrategy");
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public String getName() {
        return "PayPal";
    }
}
