package bank;

public class Transaction {
    // attributes
    private String date;
    protected double amount;
    private String description;

    // default constructor Transaction()
    public Transaction () {
        this.date = "01/01/1990";
        this.amount = 0.;
        this.description = "initialization";
    }

    // copy constructor Transaction(Transaction)
    public Transaction (Transaction transaction) {
        this.date = transaction.date;
        this.amount = transaction.amount;
        this.description = transaction.description;
    }

    // constructor Transaction(String, double, String)
    public Transaction (String date, double amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
}
