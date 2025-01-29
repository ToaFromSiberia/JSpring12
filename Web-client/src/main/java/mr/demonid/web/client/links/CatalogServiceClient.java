package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Обращение к микросервису Catalog-service.
 */
@FeignClient(name = "CATALOG-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface CatalogServiceClient {

    @GetMapping("/api/catalog/get-all")
    ResponseEntity<List<ProductInfo>> getAllProducts();

    @GetMapping("/api/catalog/get-product/{id}")
    ResponseEntity<ProductInfo> getProductById(@PathVariable Long id);

    @GetMapping("/api/catalog/get-by-category/{category}")
    ResponseEntity<List<ProductInfo>> getProductsByCategory(@PathVariable String category);

    @GetMapping("/api/catalog/get-categories")
    ResponseEntity<List<String>> getCategories();

}

