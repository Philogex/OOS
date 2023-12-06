package bank;

/**
 * Interface includes functions for caluculating a bill of a Transaction
 */
public interface CalculateBill {
    /**
     * calculates amount of Transaction with taxes, etc.
     * @return amount of Transaction with deductions
     */
    double calculate();
}
