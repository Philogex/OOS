package bank;

public class Payment extends Transaction {
    // attributes
    private double incomingInterest;
    private double outgoingInterest;

    // default constructor Payment()
    public Payment() {
        super();
        this.set_incomingInterest(0.);
        this.set_outgoingInterest(0.);
    }

    // copy constructor Payment(Payment)
    public Payment (Payment payment) {
        super(payment);
        this.set_incomingInterest(payment.incomingInterest);
        this.set_outgoingInterest(payment.outgoingInterest);
    }

    // constructor Payment(String, double, String, double, double)
    public Payment (String date, double amount, String description, double incomingInterest, double outgoingInterest) {
        super(date, amount, description);

        if(this.get_incomingInterest() < 0. || this.get_incomingInterest() > 1.) {
            System.out.println("incomingInterest cannot be outside the range [0, 1]. Setting Interest to 0");
            incomingInterest = 0.;
        }
        if(this.get_outgoingInterest() < 0. || this.get_outgoingInterest() > 1.) {
            System.out.println("outgoingInterest cannot be outside the range [0, 1]. Setting Interest to 0");
            outgoingInterest = 0.;
        }

        this.set_outgoingInterest(incomingInterest);
        this.set_outgoingInterest(outgoingInterest);
    }

    //getters
    public double get_incomingInterest() {
        return incomingInterest;
    }

    public double get_outgoingInterest() {
        return outgoingInterest;
    }

    //setters
    public void set_incomingInterest(double p_incomingInterest) {
        this.incomingInterest = p_incomingInterest;
    }

    public void set_outgoingInterest(double p_outgoingInterest) {
        this.outgoingInterest = p_outgoingInterest;
    }
}
