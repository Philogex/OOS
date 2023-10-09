package bank;

public class Transfer extends Transaction{
    // attributes
    private String sender;
    private String recipient;

    // default constructor Transfer()
    public Transfer() {
        super();
        this.sender = "initialization";
        this.recipient = "initialization";
    }

    // copy constructor Transfer(Transfer)
    public Transfer (Transfer transfer) {
        super(transfer);
        this.sender = transfer.sender;
        this.recipient = transfer.recipient;
    }

    // constructor Transfer(String, double, String, String, String)
    public Transfer (String date, double amount, String description, String sender, String recipient) {
        super(date, amount, description);

        if(amount < 0) {
            System.out.println("Amount cannot be negative. Inverting value.");
            this.amount = -amount;
        }

        this.sender = sender;
        this.recipient = recipient;
    }
}
