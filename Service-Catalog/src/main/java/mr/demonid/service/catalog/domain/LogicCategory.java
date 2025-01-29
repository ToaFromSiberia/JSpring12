package mr.demonid.service.catalog.domain;

import org.springframework.stereotype.Component;

/**
 * Категория "Логические"
 */
@Component
public class LogicCategory extends ProductCategory {

    @Override
    public String getName() {
        return "Logic";
    }
}
