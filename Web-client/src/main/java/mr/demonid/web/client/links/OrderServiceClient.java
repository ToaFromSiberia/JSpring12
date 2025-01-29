package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Обращение к микросервису Order-service.
 */
@FeignClient(name = "ORDER-SERVICE", configuration = FeignClientConfig.class)
public interface OrderServiceClient {

    @PostMapping("/api/orders")
    ResponseEntity<UUID> createOrder(@RequestBody OrderRequest order);

}
