package mr.demonid.service.payment.config;

import mr.demonid.service.payment.cor.*;
import mr.demonid.service.payment.repository.PaymentRepository;
import mr.demonid.service.payment.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    /**
     * Создание бина проведения платежей.
     */
    @Bean
    public PaymentHandler paymentHandler(PaymentRepository paymentRepository, UserRepository userRepository) {
        return new CreateTransferHandler(paymentRepository)
                .setNext(new BalanceCheckHandler(userRepository)
                        .setNext(new MethodCheckHandler()
                                .setNext(new TransferExecutionHandler()
                                        .setNext(new ApprovedTransferHandler(paymentRepository)
                                        ))));
    }

}
