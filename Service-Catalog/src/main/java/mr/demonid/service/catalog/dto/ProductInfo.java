package mr.demonid.service.catalog.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import mr.demonid.service.catalog.domain.ProductCategory;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductInfo {
    private Long id;
    private String name;
    private BigDecimal price;
    private ProductCategory productCategory;
    private int stock;
    private String description;
    private String imageBase64;
}
