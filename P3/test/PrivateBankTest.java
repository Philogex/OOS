import bank.*;
import java.util.*;
import bank.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {
    private PrivateBank privateBank;

    @org.junit.jupiter.api.BeforeEach
    public void setup() {
        privateBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5);
    }

    @org.junit.jupiter.api.Test
    void getName() {
        assertEquals("you_will_never_get_me", privateBank.getName());
    }

    @org.junit.jupiter.api.Test
    void getIncomingInterest() {
        assertEquals(0.5, privateBank.getIncomingInterest());
    }

    @org.junit.jupiter.api.Test
    void getOutgoingInterest() {
        assertEquals(0.5, privateBank.getOutgoingInterest());
    }

    @org.junit.jupiter.api.Test
    void setName() {
        privateBank.setName("GRAAAAAAAAAH");
        assertEquals("GRAAAAAAAAAH", privateBank.getName());
    }

    @org.junit.jupiter.api.Test
    void setIncomingInterest() {
        privateBank.setIncomingInterest(1.);
        assertEquals(1., privateBank.getIncomingInterest());
    }

    @org.junit.jupiter.api.Test
    void setOutgoingInterest() {
        privateBank.setOutgoingInterest(1.);
        assertEquals(1., privateBank.getOutgoingInterest());
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        PrivateBank equalsBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5);
        PrivateBank notEqualsBank = new PrivateBank("WRONG", 0.9, 0.9);

        assertEquals(privateBank, equalsBank);
        assertNotEquals(privateBank, notEqualsBank);
    }

    @org.junit.jupiter.api.Test
    void PrivateBank() {
        String name = "THEY WONT TAKE ME ALIVE";
        double incomingInterest = 0.1;
        double outgoingInterest = 0.1;
        PrivateBank constructorBank = new PrivateBank(name, incomingInterest, outgoingInterest);
        assertEquals(name, constructorBank.getName());
        assertEquals(incomingInterest, constructorBank.getIncomingInterest());
        assertEquals(outgoingInterest, constructorBank.getOutgoingInterest());
        assertTrue(constructorBank.accountsToTransactions.isEmpty());

        PrivateBank copyBank = new PrivateBank(privateBank);
        assertEquals(copyBank, privateBank);
        assertNotSame(copyBank, privateBank);
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        assertEquals("name: you_will_never_get_me\tincoming Interest: 0.5\toutgoingInterest: 0.5\taccountsToTransactions: ", privateBank.toString());
    }

    @org.junit.jupiter.api.Test
    void createAccount() {
        //public void createAccount(String p_account)
        assertDoesNotThrow(() -> privateBank.createAccount("AAAACEOUNT"));

        assertTrue(privateBank.accountsToTransactions.containsKey("AAAACEOUNT"));

        assertThrows(AccountAlreadyExistsException.class, () -> privateBank.createAccount("AAAACEOUNT"));



        //public void createAccount(String p_account, List<Transaction> p_transactions)
        List<Transaction> transactions = new ArrayList<>();
        Payment validPayment = new Payment();
        Transfer validTransfer = new Transfer();
        transactions.add(validPayment);
        transactions.add(validTransfer);

        assertDoesNotThrow(() -> privateBank.createAccount("AAAACEOUNTT", transactions));
        assertTrue(privateBank.accountsToTransactions.containsKey("AAAACEOUNTT"));
        assertTrue(privateBank.accountsToTransactions.get("AAAACEOUNTT").contains(validPayment));
        assertTrue(privateBank.accountsToTransactions.get("AAAACEOUNTT").contains(validTransfer));
        assertThrows(AccountAlreadyExistsException.class, () -> privateBank.createAccount("AAAACEOUNT", transactions));

        transactions.add(validTransfer);
        transactions.add(validTransfer);
        assertThrows(TransactionAlreadyExistException.class, () -> privateBank.createAccount("AAAACEOUNTTTT", transactions));

        List<Transaction> transactionsAttribute = new ArrayList<>();
        Payment invalidPayment = new Payment("", 1, "description", 0.6, 0.5);
        transactionsAttribute.add(invalidPayment);
        assertThrows(TransactionAttributeException.class, () -> privateBank.createAccount("AAAACEOUNTTTTTTTTTTTTTT", transactionsAttribute));
    }

    @org.junit.jupiter.api.Test
    void containsTransaction() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = new Payment();
        Transaction validTransaction_2 = new Payment();
        Transaction validTransaction_3 = new Payment("a", 1, "", 0.5, 0.5);

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));
        //assertTrue(privateBank.containsTransaction("payment", validTransaction_2));
        assertFalse(privateBank.containsTransaction("payment", validTransaction_3));
    }

    @org.junit.jupiter.api.Test
    void addTransactionAccountDoesNotExist() {
        Transaction validTransaction = new Payment();

        assertThrows(AccountDoesNotExistException.class, () -> privateBank.addTransaction("", validTransaction));
    }

    @org.junit.jupiter.api.Test
    void addTransactionTransactionAlreadyExist() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = new Payment();

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        assertThrows(TransactionAlreadyExistException.class, () -> privateBank.addTransaction("payment", validTransaction));
    }

    @org.junit.jupiter.api.Test
    void addTransactionTransactionAttribute() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction invalidTransaction = new Payment("", 1, "", 0.5, 0.5);

        assertThrows(TransactionAttributeException.class, () -> privateBank.addTransaction("payment", invalidTransaction));
    }

    @org.junit.jupiter.api.Test
    void addTransaction() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = new Payment();

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));
    }

    @org.junit.jupiter.api.Test
    void removeTransactionAccountDoesNotExist() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = new Payment();

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        assertThrows(AccountDoesNotExistException.class, () -> privateBank.removeTransaction("payment1", validTransaction));
    }

    @org.junit.jupiter.api.Test
    void removeTransactionTransactionDoesNotExist() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = new Payment();

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        Transaction validTransaction_2 = new Payment("a", 1, "", 0.5, 0.5);

        assertThrows(TransactionDoesNotExistException.class, () -> privateBank.removeTransaction("payment", validTransaction_2));
    }

    @org.junit.jupiter.api.Test
    void removeTransaction() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = new Payment();

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        assertDoesNotThrow(() -> privateBank.removeTransaction("payment", validTransaction));
    }

    @org.junit.jupiter.api.Test
    void getAccountBalance() {
        //empty account
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));
        assertEquals(0., privateBank.getAccountBalance("payment"));

        //account with one payment into account
        Transaction validTransaction_1 = new Payment("date", 1000, "", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction_1));
        assertEquals(500., privateBank.getAccountBalance("payment"));

        //account with one payment into account and one out of account with equal value
        Transaction validTransaction_2 = new Payment("date", -1000, "", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction_2));
        assertEquals(-1000., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value and one incoming transfer
        Transaction validTransaction_3 = new Transfer("date", 1000, "", "a", "payment");
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction_3));
        assertEquals(0., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value, one incoming transfer and one outgoing transfer
        /*
         * TEST CASES DO NOT MATCH ACTUAL RESULTS
         * incoming and outgoing transfer do not allow this to be negative
         */
        Transaction validTransaction_4 = new Transfer("date", 1000, "", "payment", "b");
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction_4));
        assertEquals(-1000., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value, one incoming transfer and one outgoing transfer
        Transaction validTransaction_5 = new OutgoingTransfer("1", 1000, "", "a", "b");
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction_5));
        assertEquals(-2000., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value, one incoming transfer and one outgoing transfer
        Transaction validTransaction_6 = new IncomingTransfer("2", 1000, "", "a", "b");
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction_6));
        assertEquals(-1000., privateBank.getAccountBalance("payment"));
    }

    @org.junit.jupiter.api.Test
    void getTransactions() {
        //check without account
        assertEquals(privateBank.getTransactions("transactions"), new ArrayList<>());

        //check with account
        assertDoesNotThrow(() -> privateBank.createAccount("transactions"));
        assertEquals(privateBank.getTransactions("transactions"), new ArrayList<>());

        //check for transaction
        Transaction validTransaction = new Payment("date", 1000, "", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction));
        assertNotEquals(privateBank.getTransactions("transactions"), new ArrayList<>());
    }

    @org.junit.jupiter.api.Test
    void getTransactionsSorted() {
        //add elements
        assertDoesNotThrow(() -> privateBank.createAccount("transactions"));
        Transaction validTransaction_1 = new Payment("date", 1, "", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction_1));
        Transaction validTransaction_3 = new Payment("date", 3, "", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction_3));
        Transaction validTransaction_2 = new Payment("date", 2, "", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction_2));

        //check asc
        List<Transaction> ascending = privateBank.getTransactionsSorted("transactions", true);
        assertEquals(validTransaction_1, ascending.get(0));
        assertEquals(validTransaction_2, ascending.get(1));
        assertEquals(validTransaction_3, ascending.get(2));

        //check desc
        List<Transaction> descending = privateBank.getTransactionsSorted("transactions", false);
        assertEquals(validTransaction_3, descending.get(0));
        assertEquals(validTransaction_2, descending.get(1));
        assertEquals(validTransaction_1, descending.get(2));
    }

    @org.junit.jupiter.api.Test
    void getTransactionsByType() {
        //add elements
        assertDoesNotThrow(() -> privateBank.createAccount("transactions"));
        Transaction validTransaction_1 = new Payment("date", -1000, "outgoing", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction_1));
        Transaction validTransaction_2 = new Payment("date", 1000, "incoming", 0.5, 0.5);
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction_2));
        Transaction validTransaction_3 = new OutgoingTransfer("date", 1000, "outgoing", "a", "b");
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction_3));
        Transaction validTransaction_4 = new IncomingTransfer("date", 500, "incoming", "a", "b");
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", validTransaction_4));

        //check incoming
        List<Transaction> incoming = privateBank.getTransactionsByType("transactions", true);
        System.out.println(incoming);
        assertEquals(validTransaction_2, incoming.get(0));
        assertEquals(validTransaction_4, incoming.get(1));

        //check outgoing
        List<Transaction> outgoing = privateBank.getTransactionsByType("transactions", false);
        assertEquals(validTransaction_1, outgoing.get(0));
        assertEquals(validTransaction_3, outgoing.get(1));
    }
}