package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.CategoryFactory;
import mr.demonid.service.catalog.domain.Product;
import mr.demonid.service.catalog.domain.ProductCategory;
import mr.demonid.service.catalog.dto.ProductInfo;
import mr.demonid.service.catalog.services.CategoryService;
import mr.demonid.service.catalog.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalog")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;

    /**
     * Возвращает список всех доступных товаров.
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<ProductInfo>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return getListResponseEntity(products);
    }

    /**
     * Возвращает продукт по его ID.
     */
    @GetMapping("/get-product/{id}")
    public ResponseEntity<ProductInfo> getProductById(@PathVariable Long id) {
        return productService.getProductByIdWithCategory(id)
                .map(product -> ResponseEntity.ok(convertToProductInfo(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Возвращает список категорий
     */
    @GetMapping("/get-categories")
    public ResponseEntity<List<String>> getCategories() {
        List<ProductCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories.stream().map(ProductCategory::getName).toList());
    }

    /**
     * Возвращает список товаров из заданной категории.
     * @param category Имя категории.
     */
    @GetMapping("/get-by-category/{category}")
    public ResponseEntity<List<ProductInfo>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductByCategory(category);
        return getListResponseEntity(products);
    }


    /**
     * Конвертирует список Products в список ProductInfo
     * и возвращает результат в ResponseEntity.
     */
    private ResponseEntity<List<ProductInfo>> getListResponseEntity(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ProductInfo> res = products.stream().map(this::convertToProductInfo).toList();
        return ResponseEntity.ok(res);
    }

    /**
     * Конвертирует объект Product в DTO ProductInfo.
     */
    private ProductInfo convertToProductInfo(Product product) {
        return new ProductInfo(
                product.getId(),
                product.getName(),
                product.getPrice(),
                categoryService.getCategory(product.getCategory().getName()),
                product.getStock(),
                product.getDescription(),
                productService.encodeImageToBase64(product.getImageFile())
        );
    }

}
