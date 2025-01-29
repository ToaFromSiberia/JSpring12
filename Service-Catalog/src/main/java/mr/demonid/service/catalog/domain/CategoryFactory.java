package mr.demonid.service.catalog.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Фабрика категорий.
 */
@Component
@AllArgsConstructor
public class CategoryFactory {

    private final LogicCategory logicCategory;
    private final ActionCategory actionCategory;
    private final RaceCategory raceCategory;
    private final OtherCategory otherCategory;

    public ProductCategory getCategory(String type) {
        return switch (type.toLowerCase()) {
            case "logic" -> logicCategory;
            case "action" -> actionCategory;
            case "race" -> raceCategory;
            case "other" -> otherCategory;
            default -> throw new IllegalArgumentException("Неизвестная категория: " + type);
        };
    }
}
