package bank;

public class Payment extends Transaction {
    // attributes
    private double incomingInterest;
    private double outgoingInterest;

    // default constructor Payment()
    public Payment() {
        super();
        this.incomingInterest = 0.;
        this.outgoingInterest = 0.;
    }

    // copy constructor Payment(Payment)
    public Payment (Payment payment) {
        super(payment);
        this.incomingInterest = payment.incomingInterest;
        this.outgoingInterest = payment.outgoingInterest;
    }

    // constructor Payment(String, double, String, double, double)
    public Payment (String date, double amount, String description, double incomingInterest, double outgoingInterest) {
        super(date, amount, description);

        if(incomingInterest < 0. || incomingInterest > 1.) {
            System.out.println("incomingInterest cannot be outside the range [0, 1]. Setting Interest to 0");
            incomingInterest = 0.;
        }
        if(outgoingInterest < 0. || outgoingInterest > 1.) {
            System.out.println("outgoingInterest cannot be outside the range [0, 1]. Setting Interest to 0");
            outgoingInterest = 0.;
        }

        this.incomingInterest = incomingInterest;
        this.outgoingInterest = outgoingInterest;
    }
}
