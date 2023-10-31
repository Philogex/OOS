package bank;

import bank.exceptions.*;

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
     * default amount: 1000 <br>
     * default description: "initialization" <br>
     * default sender: "initialization" <br>
     * default recipient: "initialization"
     * @see Transaction#Transaction()
     */
    public Transfer() {
        super();
        this.setSender("initialization_1");
        this.setRecipient("initialization_2");
    }

    /**
     * copy constructor
     * @param p_transfer object to be used as copy reference
     * @see Transaction#Transaction(Transaction p_transfer)
     */
    public Transfer (Transfer p_transfer) {
        super(p_transfer);
        this.setSender(p_transfer.sender);
        this.setRecipient(p_transfer.recipient);
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
        this.setSender(p_sender);
        this.setRecipient(p_recipient);
    }

    /**
     * getter for sender
     * @return returns class:Transfer attribute value of sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * getter for recipient
     * @return returns class:Transfer attribute value of recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * setter for sender
     * @param p_sender value to be assigned to object
     */
    public void setSender(String p_sender) {
        this.sender = p_sender;
    }

    /**
     * setter for amount
     * @param p_amount value to be assigned to object
     */
    @Override
    public void setAmount(double p_amount) {
        try {
            if(p_amount < 0) {
                throw new TransactionAttributeValidationException("amount negative");
            }
            super.setAmount(p_amount);
        } catch (TransactionAttributeValidationException e) {
            System.out.println(e + " for object:\n" + this + "\ninverting value.");
            super.setAmount(-p_amount);
        }
    }

    /**
     * setter for recipient
     * @param p_recipient value to be assigned to object
     */
    public void setRecipient(String p_recipient) {
        this.recipient = p_recipient;
    }

    /**
     * implementation of calculate()
     * @return amount after taxes >:(
     */
    @Override
    public double calculate() {
        return this.getAmount();
    }

    /**
     * Date, Description, Amount, sender, recipient of this object to a string
     * @return string is separated by \t
     */
    @Override
    public String toString() {
        return String.format("%s\tSender: %s\tRecipient: %s", super.toString(), this.getSender(), this.getRecipient());
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
        if(!(p_object instanceof Transfer)) {
            return false;
        }
        //cast and compare
        Transfer object = (Transfer) p_object;
        return this.getRecipient().equals(object.getRecipient()) &&
                this.getSender().equals(object.getSender());
    }

    /**
     * checks attributes of transaction for validity <br>
     * date has to be set <br>
     * amount not 0. <br>
     * description can be empty <br>
     * sender has to be set <br>
     * recipient has to be set <br>
     * sender and recipient not the same
     * @return true if attributes are valid or false if not
     */
    @Override
    public boolean validateTransactionAttributes() {
        if(!super.validateTransactionAttributes())
            return false;

        return this.getRecipient() != null && !this.getRecipient().isEmpty() && this.getSender() != null && !this.getSender().isEmpty() && !this.getSender().equals(this.getRecipient());
    }

    /**
     * implementation of abstract method, but does nothing for Transfer objects
     * @param p_incomingInterest interest rates for incoming transactions
     * @param p_outgoingInterest interest rate for outgoing transactions
     */
    @Override
    public void overwriteInterest(double p_incomingInterest, double p_outgoingInterest) {}
}