package bank.exceptions;

/**
 * exception handling for base class Attributes of Payment and Transfer of Transaction
 */
public class TransactionAttributeValidationException extends Exception {
    /**
     * attribute of child class is invalid
     * @param p_message message to be displayed
     */
    public TransactionAttributeValidationException(String p_message) {
        super(p_message);
    }
}
