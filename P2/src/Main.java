import bank.Payment;
import bank.Transfer;

public class Main {
    public static void main(String[] args) {
        // Payment
        // default constructor Payment
        Payment payment_default = new Payment();

        // copy constructor Payment
        Payment payment_copy = new Payment(payment_default);

        // constructor
        Payment payment1 = new Payment("11/11/1111", 1000., "eine tausent", 0., 0.);

        // Transfer
        // default constructor Transfer
        Transfer transfer_default = new Transfer();

        // copy constructor Transfer
        Transfer transfer_copy = new Transfer(transfer_default);

        // constructor
        Transfer transfer1 = new Transfer("11/11/1111", 1000, "eine tausent", "ich", "mir");


        // error testing
        // Payment: incomingInterest out of Range [0, 1]
        System.out.println("Payment: incomingInterest out of Range [0, 1]");
        Payment payment2 = new Payment("11/11/1111", 1000., "eine tausent", -0.1, 0.);

        // Payment: outgoingInterest out of Range [0, 1]
        System.out.println("Payment: outgoingInterest out of Range [0, 1]");
        Payment payment3 = new Payment("11/11/1111", -1000., "eine tausent", 0., 1.1);

        // Transfer: amount in negative
        System.out.println("Transfer: amount in negative");
        Transfer transfer2 = new Transfer("11/11/1111", -1000, "eine tausent", "ich", "mir");


        //testing calculate() Payment, outgoingInterest
        Payment payment4 = new Payment("11/11/1111", -1000., "eine tausent", 0., 0.9);

        //testing calculate() Payment, incomingInterest
        Payment payment5 = new Payment("11/11/1111", 1000., "eine tausent", 0.1, 0.9);

        // Test calculate(): Transfer
        System.out.println("Amount for Transfer: 1000 == " + transfer1.calculate());
        // Test calculate(): Payment, outgoingInterest
        System.out.println("Amount for Payment after outgoing Interest: -1900.0 == " + payment4.calculate());
        // Test calculate(): Payment, incomingInterest
        System.out.println("Amount for Payment after incoming Interest: 900.0 == " + payment5.calculate());

        // Test equals(): Transfer
        System.out.println("Two exact same Transfers are the same: " + transfer_default.equals(transfer_copy));

        // Test equals(): Payment
        System.out.println("Two exact same Payments are the same: " + payment_default.equals(payment_copy));

        // Test toString(): Transfer
        System.out.println("Default Transfer print: " + transfer_default);

        // Test toString(): Payment
        System.out.println("Default Transfer print: " + payment_default);
    }
}