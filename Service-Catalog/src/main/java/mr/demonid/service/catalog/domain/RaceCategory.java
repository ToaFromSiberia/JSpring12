package mr.demonid.service.catalog.domain;

import org.springframework.stereotype.Component;

/**
 * Категория "Гонки"
 */
@Component
public class RaceCategory extends ProductCategory {

    @Override
    public String getName() {
        return "Race";
    }
}
