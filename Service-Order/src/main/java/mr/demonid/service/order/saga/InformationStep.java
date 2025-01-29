package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.services.InformationService;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * Шаг: Информирование пользователя о статусе сделки.
 */
@AllArgsConstructor
public class InformationStep implements SagaStep<SagaContext> {

    private InformationService informationService;

    @Override
    public void execute(SagaContext context) throws SagaStepException {
        informationService.sendMessage("Покупка совершена. Ваш заказ в ближайшее время перейдет в службу доставки.");
    }

    @Override
    public void rollback(SagaContext context) {

    }
}
