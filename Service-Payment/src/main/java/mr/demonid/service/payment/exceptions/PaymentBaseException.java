package mr.demonid.service.payment.exceptions;

public class PaymentBaseException extends RuntimeException {

    public PaymentBaseException() {
        super();
    }

    public PaymentBaseException(String message) {
        super(message);
    }

}
