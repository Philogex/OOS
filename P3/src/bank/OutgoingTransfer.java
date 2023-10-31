package bank;

/**
 * outgoing transfers
 */
public class OutgoingTransfer extends Transfer {
    /**
     * constructor for all attributes of object
     * @param p_date date
     * @param p_amount amount
     * @param p_description description
     * @param p_sender sender
     * @param p_recipient recipient
     */
    public OutgoingTransfer(String p_date, double p_amount, String p_description, String p_sender, String p_recipient) {
        super(p_date, p_amount, p_description, p_sender, p_recipient);
    }

    /**
     * calculates change in value for account
     * @return negative value of amount
     */
    @Override
    public double calculate() {
        return -this.getAmount();
    }
}