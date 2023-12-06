package bank;

import bank.exceptions.*;
import com.google.gson.annotations.SerializedName;

/**
 * class for Transactions between banks
 */
public class Payment extends Transaction {
    /**
     * interest rate for incoming transaction
     */
    private double incomingInterest;

    /**
     * interest rate for outgoing transaction
     */
    private double outgoingInterest;

    /**
     * default constructor <br>
     * default date: 01/01/1990 <br>
     * default amount: 1000 <br>
     * default description: "initialization" <br>
     * default incoming interest: 0.5 <br>
     * default outgoing interest: 0.5
     * @see Transaction#Transaction()
     */
    public Payment() throws TransactionAttributeException {
        super();
        this.setIncomingInterest(0.5);
        this.setOutgoingInterest(0.5);
    }

    /**
     * copy constructor
     * @param p_payment object to be used as copy reference
     * @see Transaction#Transaction(Transaction p_payment)
     */
    public Payment (Payment p_payment) throws TransactionAttributeException {
        super(p_payment);
        this.setIncomingInterest(p_payment.incomingInterest);
        this.setOutgoingInterest(p_payment.outgoingInterest);
    }

    /**
     * constructor for all attributes
     * @param p_date date to be assigned to attribute:date
     * @param p_amount amount to be assigned to attribute:amount
     * @param p_description description to be assigned to attribute:description
     * @param p_incomingInterest incoming interest to be assigned to attribute:incomingInterest
     * @param p_outgoingInterest outgoing interest to be assigned to attribute:outgoingInterest
     * @see Transaction#Transaction(String p_date, double p_amount, String p_description)
     */
    public Payment (String p_date, double p_amount, String p_description, double p_incomingInterest, double p_outgoingInterest) throws TransactionAttributeException {
        super(p_date, p_amount, p_description);
        this.setOutgoingInterest(p_incomingInterest);
        this.setOutgoingInterest(p_outgoingInterest);
    }

    /**
     * getter for incomingInterest
     * @return returns class:Payment attribute value of incomingInterest
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * getter for outgoingInterest
     * @return returns class:Payment attribute value of outgoingInterest
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * setter for incoming Interest
     * @param p_incomingInterest value to be assigned to object. checks if value in [0., 1.]
     */
    public void setIncomingInterest(double p_incomingInterest) throws TransactionAttributeException
    {
        if(this.getIncomingInterest() < 0. || this.getIncomingInterest() > 1.)
            throw new TransactionAttributeException();
        this.incomingInterest = p_incomingInterest;
    }

    /**
     * setter for outoingInterest
     * @param p_outgoingInterest value to be assigned to object. checks if value in [0., 1.]
     */
    public void setOutgoingInterest(double p_outgoingInterest) throws TransactionAttributeException
    {
        if(this.getOutgoingInterest() < 0. || this.getOutgoingInterest() > 1.)
            throw new TransactionAttributeException();
        this.outgoingInterest = p_outgoingInterest;
    }

    /**
     * implementation of calculate()
     * @return amount after taxes >:(
     */
    @Override
    public double calculate() {
        double amount = this.getAmount();
        //i don't need to check if interest is equal to 0, since the calculation would still be correct
        if(amount > 0) {
            return amount * (1 - this.getIncomingInterest());
        }
        else {
            return amount * (1 + this.getOutgoingInterest());
        }
    }

    /**
     * Date, Description, Amount, incomingInterest, outgoingInterest of this object to a string
     * @return string is separated by \t
     */
    @Override
    public String toString() {
        return String.format("%s\tincoming Interest: %s\toutgoing Interest: %s", super.toString(), this.getIncomingInterest(), this.getOutgoingInterest());
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
        if(!(p_object instanceof Payment)) {
            return false;
        }
        //cast and compare
        Payment object = (Payment) p_object;
        return this.getIncomingInterest() == object.getIncomingInterest() &&
                this.getOutgoingInterest() == object.getOutgoingInterest();
    }

    /**
     * checks attributes of transaction for validity <br>
     * date has to be set <br>
     * amount not 0. <br>
     * description can be empty <br>
     * Payment.incomingInterest in range [0., 1.] <br>
     * Payment.outgoingInterest in range [0., 1.]
     * @return true if attributes are valid or false if not
     */
    @Override
    public boolean validateTransactionAttributes() {
        if(!super.validateTransactionAttributes())
            return false;

        return this.getIncomingInterest() <= 1. && this.getIncomingInterest() >= 0. && this.getOutgoingInterest() <= 1. && this.getOutgoingInterest() >= 0.;
    }
}
