package bank;

public class Transaction {
    // attributes
    private String date;
    protected double amount;
    private String description;

    // default constructor Transaction()
    public Transaction () {
        this.set_date("01/01/1990");
        this.set_amount(0.);
        this.set_description("initialization");
    }

    // copy constructor Transaction(Transaction)
    public Transaction (Transaction transaction) {
        this.set_date(transaction.date);
        this.set_amount(transaction.amount);
        this.set_description(transaction.description);
    }

    // constructor Transaction(String, double, String)
    public Transaction (String date, double amount, String description) {
        this.set_date(date);
        this.set_amount(amount);
        this.set_description(description);
    }

    //getters
    public String get_date() {
        return this.date;
    }
    public double get_amount() {
        return this.amount;
    }

    public String get_description() {
        return this.description;
    }

    //setters
    public void set_date(String p_date) {
        this.date = p_date;
    }

    public void set_amount(double p_amount) {
        this.amount = p_amount;
    }

    public void set_description(String p_description) {
        this.description = p_description;
    }
}
