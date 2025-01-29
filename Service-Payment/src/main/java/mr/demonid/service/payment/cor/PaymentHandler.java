package mr.demonid.service.payment.cor;

import mr.demonid.service.payment.dto.PaymentContext;

/**
 * Шаблон для классов паттерна Chain of Responsibility
 */
public abstract class PaymentHandler {

    protected PaymentHandler nextHandler;

    /**
     * Добавляет ссылку на следующий обработчик в цепи.
     */
    public PaymentHandler setNext(PaymentHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }

    /**
     * Выполнение текущего обработчика и переход к следующему.
     */
    public void run(PaymentContext context) {
        handle(context);
        if (nextHandler != null) {
            nextHandler.run(context);
        }
    }

    /**
     * Пользовательская функция.
     */
    protected abstract void handle(PaymentContext context);

}
