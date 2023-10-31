package bank;

import bank.exceptions.*;

/**
 * base class for Transactions of any kind
 */
public abstract class Transaction implements CalculateBill {
    /**
     * date of transaction
     */
    private String date;

    /**
     * amount of transaction
     */
    private double amount;

    /**
     * description of transaction
     */
    private String description;

    /**
     * default constructor <br>
     * default date: 01/01/1990 <br>
     * default amount: 0.5 <br>
     * default description: "initialization"
     */
    public Transaction () {
        this.setDate("01/01/1990");
        this.setAmount(1000);
        this.setDescription("initialization");
    }

    /**
     * copy constructor
     * @param p_transaction object to be used as copy reference
     */
    public Transaction (Transaction p_transaction) {
        this.setDate(p_transaction.date);
        this.setAmount(p_transaction.amount);
        this.setDescription(p_transaction.description);
    }

    /**
     * constructor for all attributes
     * @param p_date date to be assigned to attribute:date
     * @param p_amount amount to be assigned to attribute:amount
     * @param p_description description to be assigned to attribute:description
     */
    public Transaction (String p_date, double p_amount, String p_description) {
        this.setDate(p_date);
        this.setAmount(p_amount);
        this.setDescription(p_description);
    }

    /**
     * getter for date
     * @return returns class:Transaction attribute value of date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * getter for amount
     * @return returns class:Transaction attribute value of amount
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * getter for description
     * @return returns class:Transaction attribute value of description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * setter for date
     * @param p_date value to be assigned to object
     */
    public void setDate(String p_date) {
        this.date = p_date;
    }

    /**
     * setter for amount
     * @param p_amount value to be assigned to object
     */
    public void setAmount(double p_amount) {
        this.amount = p_amount;
    }

    /**
     * setter for description
     * @param p_description value to be assigned to object
     */
    public void setDescription(String p_description) {
        this.description = p_description;
    }

    /**
     * override of Object toString()
     * @return Date and Description with \t inbetween and at the end
     */
    @Override
    public String toString() {
        return String.format("Amount: %s\t Date: %s\tDescription: %s\t", this.calculate(), this.getDate(), this.getDescription());
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
        //check if current object has same class as comparison object, and if comparison object is initialized
        if(!(p_object instanceof Transaction)) {
            return false;
        }
        //cast and compare
        Transaction object = (Transaction) p_object;
        return this.getDescription().equals(object.getDescription()) &&
                this.getAmount() == object.getAmount() &&
                this.getDate().equals(object.getDate());
    }

    /**
     * checks attributes of transaction for validity <br>
     * date has to be set <br>
     * amount not 0. <br>
     * description can be empty
     * @return true if attributes are valid or false if not
     */
    public boolean validateTransactionAttributes() {
        return this.getDate() != null && !this.getDate().isEmpty() && this.getDescription() != null && this.getAmount() != 0.;
    }

    /**
     * overwrites attributes incomingInterest and outgoingInterest in case of a payment
     * @param p_incomingInterest value to overwrite incoming interest rate
     * @param p_outgoingInterest value to overwrite incoming interest rate
     */
    public abstract void overwriteInterest(double p_incomingInterest, double p_outgoingInterest);
}