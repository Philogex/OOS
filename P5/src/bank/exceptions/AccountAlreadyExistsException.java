package bank.exceptions;

/**
 * account already exists exception class
 */
public class AccountAlreadyExistsException extends Exception {
    /**
     * account already exists
     */
    public AccountAlreadyExistsException() {
        super("Account already exists.");
    }
}


