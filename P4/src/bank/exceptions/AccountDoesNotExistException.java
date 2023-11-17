package bank.exceptions;

/**
 * account does not exist exception class
 */
public class AccountDoesNotExistException extends Exception {
    /**
     * account does not exist
     */
    public AccountDoesNotExistException() {
        super("Account does not exist.");
    }
}
