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

        //constructor
        Transfer transfer1 = new Transfer("11/11/1111", 1000, "eine tausent", "ich", "mir");


        // error testing
        // Payment: incomingInterest out of Range [0, 1]
        System.out.println("Payment: incomingInterest out of Range [0, 1]");
        Payment payment2 = new Payment("11/11/1111", 1000., "eine tausent", -0.1, 0.);

        // Payment: outgoingInterest out of Range [0, 1]
        System.out.println("Payment: outgoingInterest out of Range [0, 1]");
        Payment payment3 = new Payment("11/11/1111", 1000., "eine tausent", 0., 1.1);

        // Transfer: amount in negative
        System.out.println("Transfer: amount in negative");
        Transfer transfer2 = new Transfer("11/11/1111", -1000, "eine tausent", "ich", "mir");
    }
}