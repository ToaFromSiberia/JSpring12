package mr.demonid.service.payment.cor;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.UserEntity;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import mr.demonid.service.payment.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Шаг: проверка баланса.
 */
@AllArgsConstructor
public class BalanceCheckHandler extends PaymentHandler {

    private UserRepository userRepository;


    @Override
    protected void handle(PaymentContext context) {
        System.out.println("-- BalanceCheckHandler()");
        Optional<UserEntity> user = userRepository.findById(context.getUserId());
        if (user.isEmpty()) {
            throw new PaymentStepException("Пользователь не найден");
        }
        BigDecimal balance = user.get().getAmount();
        System.out.println("  -- balance: " + balance);
        System.out.println("  -- amount: " + context.getAmount());
        if (context.getAmount().compareTo(balance) > 0) {
            throw new PaymentStepException("Недостаточно средств");
        }
    }

}
