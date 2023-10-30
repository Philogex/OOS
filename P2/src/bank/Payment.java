package bank;

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
     * default amount: 0. <br>
     * default description: "initialization" <br>
     * default incoming interest: 0. <br>
     * default outgoing interest: 0.
     * @see Transaction#Transaction()
     */
    public Payment() {
        super();
        this.set_incomingInterest(0.);
        this.set_outgoingInterest(0.);
    }

    /**
     * copy constructor
     * @param p_payment object to be used as copy reference
     * @see Transaction#Transaction(Transaction p_payment)
     */
    public Payment (Payment p_payment) {
        super(p_payment);
        this.set_incomingInterest(p_payment.incomingInterest);
        this.set_outgoingInterest(p_payment.outgoingInterest);
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
    public Payment (String p_date, double p_amount, String p_description, double p_incomingInterest, double p_outgoingInterest) {
        super(p_date, p_amount, p_description);
        this.set_outgoingInterest(p_incomingInterest);
        this.set_outgoingInterest(p_outgoingInterest);
    }

    /**
     * getter for incomingInterest
     * @return returns class:Payment attribute value of incomingInterest
     */
    public double get_incomingInterest() {
        return incomingInterest;
    }

    /**
     * getter for outgoingInterest
     * @return returns class:Payment attribute value of outgoingInterest
     */
    public double get_outgoingInterest() {
        return outgoingInterest;
    }

    /**
     * setter for incoming Interest
     * @param p_incomingInterest value to be assigned to object. checks if value in [0., 1.]
     */
    public void set_incomingInterest(double p_incomingInterest)
    {
        if(this.get_incomingInterest() < 0. || this.get_incomingInterest() > 1.) {
            System.out.println("incomingInterest cannot be outside the range [0, 1]. Setting Interest to 0");
            p_incomingInterest = 0.;
        }
        this.incomingInterest = p_incomingInterest;
    }

    /**
     * setter for outoingInterest
     * @param p_outgoingInterest value to be assigned to object. checks if value in [0., 1.]
     */
    public void set_outgoingInterest(double p_outgoingInterest)
    {
        if(this.get_outgoingInterest() < 0. || this.get_outgoingInterest() > 1.) {
            System.out.println("outgoingInterest cannot be outside the range [0, 1]. Setting Interest to 0");
            p_outgoingInterest = 0.;
        }
        this.outgoingInterest = p_outgoingInterest;
    }

    /**
     * implementation of calculate()
     * @return amount after taxes >:(
     */
    @Override
    public double calculate() {
        double amount = this.get_amount();
        //i don't need to check if interest is greater than 0, since the calculation would still be correct
        if(amount > 0) {
            return amount * (1 - this.get_incomingInterest());
        }
        else {
            return amount * (1 + this.get_outgoingInterest());
        }
    }

    /**
     * Date, Description, Amount, incomingInterest, outgoingInterest of this object to a string
     * @return string is separated by \t
     */
    @Override
    public String toString() {
        return String.format("%s\tincoming Interest: %s\toutgoing Interest: %s", super.toString(), this.get_incomingInterest(), this.get_outgoingInterest());
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
        Payment object = (Payment) p_object;
        return this.get_incomingInterest() == object.get_incomingInterest() &&
                this.get_outgoingInterest() == object.get_outgoingInterest();
    }
}
