package bank.exceptions;

/**
 * transaction already exists exception class
 */
public class TransactionAlreadyExistException extends Exception {
    /**
     * transaction alrady exists
     */
    public TransactionAlreadyExistException() {
        super("Transaction already exists.");
    }
}
