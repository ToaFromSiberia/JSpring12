package mr.demonid.service.payment.config;

import mr.demonid.service.payment.domain.UserEntity;
import mr.demonid.service.payment.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.List;

/**
 * Заполняем таблицу для теста.
 */
@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner initData(UserRepository userRepository) {
        return args -> {
            userRepository.saveAll(List.of(
                new UserEntity(1L, BigDecimal.valueOf(5000.00)),
                new UserEntity(2L, BigDecimal.valueOf(5000.00)),
                new UserEntity(3L, BigDecimal.valueOf(5000.00))
            ));
        };
    }
}
