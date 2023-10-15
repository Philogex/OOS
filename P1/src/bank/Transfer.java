package bank;

public class Transfer extends Transaction{
    // attributes
    private String sender;
    private String recipient;

    // default constructor Transfer()
    public Transfer() {
        super();
        this.set_sender("initialization");
        this.set_recipient("initialization");
    }

    // copy constructor Transfer(Transfer)
    public Transfer (Transfer transfer) {
        super(transfer);
        this.set_sender(transfer.sender);
        this.set_recipient(transfer.recipient);
    }

    // constructor Transfer(String, double, String, String, String)
    public Transfer (String date, double amount, String description, String sender, String recipient) {
        super(date, amount, description);

        if(this.get_amount() < 0) {
            System.out.println("Amount cannot be negative. Inverting value.");
            this.set_amount(-amount);
        }

        this.set_sender(sender);
        this.set_recipient(recipient);
    }

    //getters
    public String get_sender() {
        return sender;
    }

    public String get_recipient() {
        return recipient;
    }

    //setters
    public void set_sender(String sender) {
        this.sender = sender;
    }

    public void set_recipient(String recipient) {
        this.recipient = recipient;
    }
}
