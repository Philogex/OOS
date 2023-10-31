package bank.exceptions;

/**
 * transaction does not exist exception class
 */
public class TransactionDoesNotExistException extends Exception {
    /**
     * transaction does not exist
     */
    public TransactionDoesNotExistException() {
        super("Transaction does not exist.");
    }
}
