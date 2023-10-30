package bank;

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
     * default amount: 0. <br>
     * default description: "initialization"
     */
    public Transaction () {
        this.set_date("01/01/1990");
        this.set_amount(0.);
        this.set_description("initialization");
    }

    /**
     * copy constructor
     * @param p_transaction object to be used as copy reference
     */
    public Transaction (Transaction p_transaction) {
        this.set_date(p_transaction.date);
        this.set_amount(p_transaction.amount);
        this.set_description(p_transaction.description);
    }

    /**
     * constructor for all attributes
     * @param p_date date to be assigned to attribute:date
     * @param p_amount amount to be assigned to attribute:amount
     * @param p_description description to be assigned to attribute:description
     */
    public Transaction (String p_date, double p_amount, String p_description) {
        this.set_date(p_date);
        this.set_amount(p_amount);
        this.set_description(p_description);
    }

    /**
     * getter for date
     * @return returns class:Transaction attribute value of date
     */
    public String get_date() {
        return this.date;
    }

    /**
     * getter for amount
     * @return returns class:Transaction attribute value of amount
     */
    public double get_amount() {
        return this.amount;
    }

    /**
     * getter for description
     * @return returns class:Transaction attribute value of description
     */
    public String get_description() {
        return this.description;
    }

    /**
     * setter for date
     * @param p_date value to be assigned to object
     */
    public void set_date(String p_date) {
        this.date = p_date;
    }

    /**
     * setter for amount
     * @param p_amount value to be assigned to object
     */
    public void set_amount(double p_amount) {
        this.amount = p_amount;
    }

    /**
     * setter for description
     * @param p_description value to be assigned to object
     */
    public void set_description(String p_description) {
        this.description = p_description;
    }

    /**
     * override of Object toString()
     * @return Date and Description with \t inbetween and at the end
     */
    @Override
    public String toString() {
        return String.format("Amount: %s\t Date: %s\tDescription: %s\t", this.calculate(), this.get_date(), this.get_description());
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
        return this.get_description().equals(object.get_description()) &&
                this.get_amount() == object.get_amount() &&
                this.get_date().equals(object.get_date());
    }
}
