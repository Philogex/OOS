import com.google.gson.*;
import bank.*;

import java.io.File;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionSerializerTest {
    static String dir = "./test_data";

    @org.junit.jupiter.api.AfterAll
    public static void cleanup() {
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

    @org.junit.jupiter.api.Test
    void testPaymentSerialization() {
        PrivateBank privateBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5, dir);

        assertDoesNotThrow(() -> privateBank.createAccount("payment"));
        Transaction validTransaction = new Payment();
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));

        //get transactions for account payment
        List<Transaction> transactions = privateBank.accountsToTransactions.get("payment");

        //check for one transaction
        assertEquals(1, transactions.size());

        //get first transaction
        Transaction transaction = transactions.get(0);

        //new gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Payment.class, new TransactionSerializer());
        Gson gson = gsonBuilder.create();

        //serialize
        String json = gson.toJson(transaction);

        String comparison = "{" +
                "\"CLASSNAME\":\"Payment\"," +
                "\"INSTANCE\":{" +
                "\"incomingInterest\":0.5," +
                "\"outgoingInterest\":0.5," +
                "\"date\":\"01/01/1990\"," +
                "\"amount\":1000.0," +
                "\"description\":\"initialization\"" +
                "}" + "}";

        //compare json strings
        assertEquals(comparison, json);

        //get json object
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        //check CLASSNAME attribute
        JsonElement classNameElement = jsonObject.get("CLASSNAME");
        assertNotNull(classNameElement, "CLASSNAME property is missing in the JSON");
        assertEquals("Payment", classNameElement.getAsString());
    }

    @org.junit.jupiter.api.Test
    void testPaymentDeserialization() {
        PrivateBank privateBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5, dir);

        assertDoesNotThrow(() -> privateBank.createAccount("payment"));
        Payment validTransaction = new Payment();
        assertDoesNotThrow(() -> privateBank.addTransaction("payment", validTransaction));

        //get transactions for account payment
        List<Transaction> transactions = privateBank.accountsToTransactions.get("payment");

        //check for one transaction
        assertEquals(1, transactions.size());

        //get first transaction
        Transaction transaction = transactions.get(0);

        //new gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Payment.class, new TransactionSerializer());
        Gson gson = gsonBuilder.create();

        //serialize
        String json = gson.toJson(transaction);

        String comparison = "{" +
                "\"CLASSNAME\":\"Payment\"," +
                "\"INSTANCE\":{" +
                "\"incomingInterest\":0.5," +
                "\"outgoingInterest\":0.5," +
                "\"date\":\"01/01/1990\"," +
                "\"amount\":1000.0," +
                "\"description\":\"initialization\"" +
                "}" + "}";

        //compare json strings
        assertEquals(comparison, json);

        //deserialize
        Payment deserializedTransaction = gson.fromJson(json, Payment.class);

        //check attributes
        assertEquals(0.5, deserializedTransaction.getIncomingInterest());
        assertEquals(0.5, deserializedTransaction.getOutgoingInterest());
        assertEquals("01/01/1990", deserializedTransaction.getDate());
        assertEquals(1000, deserializedTransaction.getAmount());
        assertEquals("initialization", deserializedTransaction.getDescription());
    }

    /*
    //commented out bc of private class
    @org.junit.jupiter.api.Test
    void testReadWriteAccounts() {
        PrivateBank privateBank = new PrivateBank("you_will_never_get_me", 0.5, 0.5, dir);

        String name = "payment";

        assertDoesNotThrow(() -> privateBank.createAccount(name));
        Transaction validTransaction_1 = new Payment();
        assertDoesNotThrow(() -> privateBank.addTransaction(name, validTransaction_1));
        Transaction validTransaction_2 = new Transfer();
        assertDoesNotThrow(() -> privateBank.addTransaction(name, validTransaction_2));
        Transaction validTransaction_3 = new OutgoingTransfer("01/01/1990", 1500, "AAAAAA", "payment", "a");
        assertDoesNotThrow(() -> privateBank.addTransaction(name, validTransaction_3));
        Transaction validTransaction_4 = new IncomingTransfer("01/01/1990", 1500, "AAAAAAAA", "payment", "ab");
        assertDoesNotThrow(() -> privateBank.addTransaction(name, validTransaction_4));

        //write account data
        assertDoesNotThrow(() -> privateBank.writeAccount(name));

        //check read data
        assertDoesNotThrow(privateBank::readAccounts);
        assertTrue(privateBank.accountsToTransactions.containsKey(name));
        List<Transaction> readTransactions = privateBank.accountsToTransactions.get(name);
        assertDoesNotThrow(privateBank::readAccounts);
        assertEquals(privateBank.accountsToTransactions.get(name).size(), readTransactions.size());
    }
    */
}