package bank;

/**
 * incoming transfers
 */
public class IncomingTransfer extends Transfer {

    /**
     * default constructor calling base default constructor
     */
    public IncomingTransfer() {
        super();
    }

    /**
     * constructor for all attributes of object
     * @param p_date date
     * @param p_amount amount
     * @param p_description description
     * @param p_sender sender
     * @param p_recipient recipient
     */
    public IncomingTransfer(String p_date, double p_amount, String p_description, String p_sender, String p_recipient) {
        super(p_date, p_amount, p_description, p_sender, p_recipient);
    }

    /**
     * calculates change in value for account
     * @return positive value of amount
     */
    @Override
    public double calculate() {
        return +super.calculate();
    }
}
