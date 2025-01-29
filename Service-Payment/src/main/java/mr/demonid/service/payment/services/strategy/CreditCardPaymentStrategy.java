package mr.demonid.service.payment.services.strategy;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.PaymentEntity;
import mr.demonid.service.payment.domain.UserEntity;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import mr.demonid.service.payment.repository.PaymentRepository;
import mr.demonid.service.payment.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Оплата по номеру кредитной карты.
 */
@Component
@AllArgsConstructor
public class CreditCardPaymentStrategy implements PaymentStrategy {

    private UserRepository userRepository;


    @Override
    public void pay(Long userId, BigDecimal amount)  throws PaymentStepException {
        Optional<UserEntity> entity = userRepository.findById(userId);
        if (entity.isEmpty()) {
            throw new PaymentStepException("Пользователь не найден");
        }
        UserEntity user = entity.get();

        BigDecimal balance = user.getAmount().subtract(amount);
        user.setAmount(balance);
        userRepository.save(user);
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public String getName() {
        return "CreditCard";
    }

    }
