import bank.*;

import java.io.File;
import java.util.*;
import bank.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {
    private PrivateBank privateBank;
    static String dir = "./test_data";

    @org.junit.jupiter.api.BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> privateBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5, dir));
    }

    @org.junit.jupiter.api.AfterEach
    public void cleanup() {
        File directory = new File(dir);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            directory.delete();
        }
    }

    @org.junit.jupiter.api.AfterEach
    public void resetBank() {
        privateBank = null;
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
        assertDoesNotThrow(() -> privateBank.setIncomingInterest(1.));
        assertEquals(1., privateBank.getIncomingInterest());
    }

    @org.junit.jupiter.api.Test
    void setOutgoingInterest() {
        assertDoesNotThrow(() -> privateBank.setOutgoingInterest(1.));
        assertEquals(1., privateBank.getOutgoingInterest());
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        PrivateBank equalsBank = null;
        PrivateBank notEqualsBank = null;
        try {
            equalsBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5, dir);
            notEqualsBank = new PrivateBank("WRONG", 0.9, 0.9, dir);
        } catch (Exception e) {}

        assertEquals(privateBank, equalsBank);
        assertNotEquals(privateBank, notEqualsBank);
    }

    @org.junit.jupiter.api.Test
    void PrivateBank() {
        String name = "THEY WONT TAKE ME ALIVE";
        double incomingInterest = 0.1;
        double outgoingInterest = 0.1;
        PrivateBank constructorBank = null;
        try {
            constructorBank = new PrivateBank(name, incomingInterest, outgoingInterest, dir);
        } catch(Exception e) {}
        assertEquals(name, constructorBank.getName());
        assertEquals(incomingInterest, constructorBank.getIncomingInterest());
        assertEquals(outgoingInterest, constructorBank.getOutgoingInterest());
        assertTrue(constructorBank.accountsToTransactions.isEmpty());

        assertDoesNotThrow(() -> assertEquals(new PrivateBank(privateBank), privateBank));
        assertDoesNotThrow(() -> assertNotSame(new PrivateBank(privateBank), privateBank));
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
        Payment validPayment  = null;
        Transfer validTransfer = null;
        try {
            validPayment = new Payment();
            validTransfer = new Transfer();
        } catch (Exception e) {}
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
        Payment invalidPayment = null;
        try {
            invalidPayment = new Payment("", 1, "description", 0.5, 0.5);
        } catch (Exception e) {}
        transactionsAttribute.add(invalidPayment);
        assertThrows(TransactionAttributeException.class, () -> privateBank.createAccount("AAAACEOUNTTTTTTTTTTTTTT", transactionsAttribute));
    }

    @org.junit.jupiter.api.Test
    void containsTransaction() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = null;
        Transaction validTransaction_3 = null;

        try {
            validTransaction = new Payment();
            validTransaction_3 = new Payment("a", 1, "", 0.5, 0.5);
        } catch (Exception e) {}

        Transaction finalValidTransaction = validTransaction;
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", finalValidTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));
        //assertTrue(privateBank.containsTransaction("payment", validTransaction_2));
        assertFalse(privateBank.containsTransaction("payment", validTransaction_3));
    }

    @org.junit.jupiter.api.Test
    void addTransactionAccountDoesNotExist() {
        Transaction validTransaction = null;
        try {
            validTransaction = new Payment();
        } catch (Exception e) {}
        Transaction finalValidTransaction = validTransaction;
        assertThrows(AccountDoesNotExistException.class, () -> privateBank.addTransaction("", finalValidTransaction));
    }

    @org.junit.jupiter.api.Test
    void addTransactionTransactionAlreadyExist() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Payment validTransaction = null;
        try {
            validTransaction = new Payment();
        } catch (Exception e) {}

        Payment finalValidTransaction = validTransaction;
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", finalValidTransaction));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        assertThrows(TransactionAlreadyExistException.class, () -> privateBank.addTransaction("payment", finalValidTransaction));
    }

    @org.junit.jupiter.api.Test
    void addTransactionTransactionAttribute() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        assertThrows(TransactionAttributeException.class, () -> privateBank.addTransaction("payment", new Payment("", 1, "", 0.5, 0.5)));
    }

    @org.junit.jupiter.api.Test
    void addTransaction() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = null;
        try {
            validTransaction = new Payment();
        } catch (Exception e) {}

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Payment()));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));
    }

    @org.junit.jupiter.api.Test
    void removeTransactionAccountDoesNotExist() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = null;
        try {
            validTransaction = new Payment();
        } catch (Exception e) {}

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Payment()));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        Transaction finalValidTransaction = validTransaction;
        assertThrows(AccountDoesNotExistException.class, () -> privateBank.removeTransaction("payment1", finalValidTransaction));
    }

    @org.junit.jupiter.api.Test
    void removeTransactionTransactionDoesNotExist() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = null;
        try {
            validTransaction = new Payment();
        } catch (Exception e) {}

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Payment()));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        assertThrows(TransactionDoesNotExistException.class, () -> privateBank.removeTransaction("payment", new Payment("a", 1, "", 0.5, 0.5)));
    }

    @org.junit.jupiter.api.Test
    void removeTransaction() {
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));

        Transaction validTransaction = null;
        try {
            validTransaction = new Payment();
        } catch (Exception e) {}

        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Payment()));
        assertTrue(privateBank.containsTransaction("payment", validTransaction));

        assertDoesNotThrow(() -> privateBank.removeTransaction("payment", new Payment()));
    }

    @org.junit.jupiter.api.Test
    void getAccountBalance() {
        //empty account
        assertDoesNotThrow(() -> privateBank.createAccount("payment"));
        assertEquals(0., privateBank.getAccountBalance("payment"));

        //account with one payment into account
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Payment("date", 1000, "a", 0.5, 0.5)));
        assertEquals(500., privateBank.getAccountBalance("payment"));

        //account with one payment into account and one out of account with equal value
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Payment("date", -1000, "a", 0.5, 0.5)));
        assertEquals(-1000., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value and one incoming transfer
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Transfer("date", 1000, "a", "a", "payment")));
        assertEquals(0., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value, one incoming transfer and one outgoing transfer
        /*
         * TEST CASES DO NOT MATCH ACTUAL RESULTS
         */
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new Transfer("date", 1000, "a", "b", "payment")));
        assertEquals(1000., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value, one incoming transfer and one outgoing transfer
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new OutgoingTransfer("1", 1000, "a", "a", "b")));
        assertEquals(0., privateBank.getAccountBalance("payment"));

        //account with one payment into account, one out of account with equal value, one incoming transfer and one outgoing transfer
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", new IncomingTransfer("2", 1000, "a", "a", "b")));
        assertEquals(1000., privateBank.getAccountBalance("payment"));
    }

    @org.junit.jupiter.api.Test
    void getTransactions() {
        //check without account
        assertEquals(privateBank.getTransactions("transactions"), new ArrayList<>());

        //check with account
        assertDoesNotThrow(() -> privateBank.createAccount("transactions"));
        assertEquals(privateBank.getTransactions("transactions"), new ArrayList<>());

        //check for transaction
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", new Payment("date", 1000, "b", 0.5, 0.5)));
        assertNotEquals(privateBank.getTransactions("transactions"), new ArrayList<>());
    }

    @org.junit.jupiter.api.Test
    void getTransactionsSorted() {
        assertDoesNotThrow(() -> privateBank.setOutgoingInterest(0.));
        assertDoesNotThrow(() -> privateBank.setIncomingInterest(0.));
        //add elements
        assertDoesNotThrow(() -> privateBank.createAccount("transactions"));
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", new Payment("date", 1, "a", 0.0, 0.0)));
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", new Payment("date", 3, "c", 0.0, 0.0)));
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", new Payment("date", 2, "b", 0.0, 0.0)));

        Transaction validTransaction_1 = null;
        Transaction validTransaction_3 = null;
        Transaction validTransaction_2 = null;

        try {
            validTransaction_1 = new Payment("date", 1, "a", 0.0, 0.0);
            validTransaction_2 = new Payment("date", 2, "b", 0.0, 0.0);
            validTransaction_3 = new Payment("date", 3, "c", 0.0, 0.0);
        } catch (Exception e) {}

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
        Transaction validTransaction_1 = null;
        Transaction validTransaction_2 = null;
        Transaction validTransaction_3 = null;
        Transaction validTransaction_4 = null;

        try {
            validTransaction_1 = new Payment("date", -1000, "outgoing", 0.5, 0.5);
            validTransaction_2 = new Payment("date", 1000, "incoming", 0.5, 0.5);
            validTransaction_3 = new OutgoingTransfer("date", 1000, "outgoing", "a", "b");
            validTransaction_4 = new IncomingTransfer("date", 500, "incoming", "a", "b");
        } catch (Exception e) {}

        Transaction finalValidTransaction_ = validTransaction_1;
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", finalValidTransaction_));
        Transaction finalValidTransaction_1 = validTransaction_2;
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", finalValidTransaction_1));
        Transaction finalValidTransaction_2 = validTransaction_3;
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", finalValidTransaction_2));
        Transaction finalValidTransaction_3 = validTransaction_4;
        assertDoesNotThrow(() -> privateBank.addTransaction("transactions", finalValidTransaction_3));

        //check incoming
        List<Transaction> incoming = privateBank.getTransactionsByType("transactions", true);
        assertEquals(validTransaction_2, incoming.get(0));
        assertEquals(validTransaction_4, incoming.get(1));

        //check outgoing
        List<Transaction> outgoing = privateBank.getTransactionsByType("transactions", false);
        assertEquals(validTransaction_1, outgoing.get(0));
        assertEquals(validTransaction_3, outgoing.get(1));
    }
}