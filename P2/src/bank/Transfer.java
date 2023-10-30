package bank;

/**
 * class for Transactions between two people
 */
public class Transfer extends Transaction {
    /**
     * sender of transaction
     */
    private String sender;

    /**
     * recipient of transaction
     */
    private String recipient;

    /**
     * default constructor<br>
     * default date: 01/01/1990 <br>
     * default amount: 0. <br>
     * default description: "initialization" <br>
     * default sender: "initialization" <br>
     * default recipient: "initialization"
     * @see Transaction#Transaction()
     */
    public Transfer() {
        super();
        this.set_sender("initialization");
        this.set_recipient("initialization");
    }

    /**
     * copy constructor
     * @param p_transfer object to be used as copy reference
     * @see Transaction#Transaction(Transaction p_transfer)
     */
    public Transfer (Transfer p_transfer) {
        super(p_transfer);
        this.set_sender(p_transfer.sender);
        this.set_recipient(p_transfer.recipient);
    }

    /**
     * constructor for all attributes
     * @param p_date date to be assigned to attribute:date
     * @param p_amount amount to be assigned to attribute:amount
     * @param p_description description to be assigned to attribute:description
     * @param p_sender sender to be assigned to attribute:sender
     * @param p_recipient recipient to be assigned to attribute:recipient
     * @see Transaction#Transaction(String p_date, double p_amount, String p_description)
     */
    public Transfer (String p_date, double p_amount, String p_description, String p_sender, String p_recipient) {
        super(p_date, p_amount, p_description);
        this.set_sender(p_sender);
        this.set_recipient(p_recipient);
    }

    /**
     * getter for sender
     * @return returns class:Transfer attribute value of sender
     */
    public String get_sender() {
        return sender;
    }

    /**
     * getter for recipient
     * @return returns class:Transfer attribute value of recipient
     */
    public String get_recipient() {
        return recipient;
    }

    /**
     * setter for sender
     * @param p_sender value to be assigned to object
     */
    public void set_sender(String p_sender) {
        this.sender = p_sender;
    }

    /**
     * setter for amount
     * @param p_amount value to be assigned to object
     * @see Transaction#amount
     */
    @Override
    public void set_amount(double p_amount) {
        if(p_amount < 0) {
            System.out.println("Amount cannot be negative. Inverting value.");
            p_amount = -p_amount;
        }
        super.set_amount(p_amount);
    }

    /**
     * setter for recipient
     * @param p_recipient value to be assigned to object
     */
    public void set_recipient(String p_recipient) {
        this.recipient = p_recipient;
    }

    /**
     * implementation of calculate()
     * @return amount after taxes >:(
     */
    @Override
    public double calculate() {
        return this.get_amount();
    }

    /**
     * Date, Description, Amount, sender, recipient of this object to a string
     * @return string is separated by \t
     */
    @Override
    public String toString() {
        return String.format("%s\tSender: %s\tRecipient: %s", super.toString(), this.get_sender(), this.get_recipient());
    }

    /**
     * override of Object equals()
     * @param p_object comparison object
     * @return returns true if object has equal values to comparison object, false otherwise
     */
    @Override
    public boolean equals(Object p_object) {
        //check if this is the same as comparison object
        if(this == p_object) {
            return true;
        }
        //super comparison check
        if(!super.equals(p_object)) {
            return false;
        }
        //check if current object has same class as comparison object, and if comparison object is initialized
        if(!(p_object instanceof Transaction)) {
            return false;
        }
        //cast and compare
        Transfer object = (Transfer) p_object;
        return this.get_recipient().equals(object.get_recipient()) &&
                this.get_sender().equals(object.get_sender());
    }
}
