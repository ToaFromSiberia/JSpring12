package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.StrategyInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "PAYMENT-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface PaymentServiceClient {

    @GetMapping("/api/payment/strategies")
    ResponseEntity<List<StrategyInfo>> getAvailableStrategies();
}
