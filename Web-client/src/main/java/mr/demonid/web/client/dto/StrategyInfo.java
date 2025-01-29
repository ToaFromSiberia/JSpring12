package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Описание стратегии оплаты
 */
@Data
@AllArgsConstructor
public class StrategyInfo {
    private String strategyName;        // имя класса стратегии (ключ)
    private String description;         // описание стратегии
}
