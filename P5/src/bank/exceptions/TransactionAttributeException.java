package bank.exceptions;

/**
 * transaction attribute exception class
 */
public class TransactionAttributeException extends Exception {
    /**
     * transaction attribute invalid
     */
    public TransactionAttributeException() {
        super("Transaction attribute error.");
    }
}
