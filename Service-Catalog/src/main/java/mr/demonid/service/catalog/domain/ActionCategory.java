package mr.demonid.service.catalog.domain;

import org.springframework.stereotype.Component;

/**
 * Категория "Экшен"
 */
@Component
public class ActionCategory extends ProductCategory {

    @Override
    public String getName() {
        return "Action";
    }
}
