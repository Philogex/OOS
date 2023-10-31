package bank;

import java.util.*;
import bank.exceptions.*;

public class PrivateBank implements Bank {
    /**
     * Name of private bank
     */
    private String name;
    /**
     * interest for deposit. value is in [0., 1.]
     */
    private double incomingInterest;
    /**
     * interest for withdrawals. value is in [0., 1.]
     */
    private double outgoingInterest;
    /**
     * 1 to n relation of accounts to transactions
     */
    public Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    /**
     * getter for name attribute
     * @return name of bank
     */
    public String getName() {
        return this.name;
    }

    /**
     * getter for incomingInterest attribute
     * @return interest rate for deposits
     */
    public double getIncomingInterest() {
        return this.incomingInterest;
    }

    /**
     * getter for outgoingInterest attribute
     * @return interest rate for withdrawals
     */
    public double getOutgoingInterest() {
        return this.outgoingInterest;
    }

    /**
     *  setter for name attribute
     * @param p_name value to be assigned to object
     */
    public void setName(String p_name) {
        this.name = p_name;
    }

    /**
     * setter for incomingInterest attribute
     * @param p_incomingInterest value to be assigned to object
     */
    public void setIncomingInterest(double p_incomingInterest) {
        this.incomingInterest = p_incomingInterest;
    }

    /**
     * setter for outgoingInterest attribute
     * @param p_outgoingInterest value to be assigned to object
     */
    public void setOutgoingInterest(double p_outgoingInterest) {
        this.outgoingInterest = p_outgoingInterest;
    }

    /**
     * constructor for all attributes apart from accountsToTransactions, which is only initialized without value
     * @param p_name name of bank
     * @param p_incomingInterest interest for deposit. value is in [0., 1.]
     * @param p_outgoingInterest interest for withdrawals. value is in [0., 1.]
     */
    public PrivateBank(String p_name, double p_incomingInterest, double p_outgoingInterest) {
        this.setName(p_name);
        this.setIncomingInterest(p_incomingInterest);
        this.setOutgoingInterest(p_outgoingInterest);
    }

    /**
     * copy constructor for all attributes apart from accountsToTransactions, which is only initialized without value
     * @param p_privateBank object to be copied
     */
    public PrivateBank(PrivateBank p_privateBank) {
        this.setName(p_privateBank.getName());
        this.setIncomingInterest(p_privateBank.getIncomingInterest());
        this.setOutgoingInterest(p_privateBank.getOutgoingInterest());
    }

    /**
     * converts all attributes of object to a string for easy readability and output
     * @return name, incomingInterest, outgoingInterest, accountsToTransactions as a string
     */
    @Override
    public String toString() {
        StringBuilder accountsToTransactions = new StringBuilder();

        for (Map.Entry<String, List<Transaction>> entry : this.accountsToTransactions.entrySet()) {
            String account = entry.getKey();
            List<Transaction> transactions = entry.getValue();

            accountsToTransactions.append(account).append(": ").append(transactions).append("\n");
        }

        return String.format("name: %s\tincoming Interest: %s\toutgoingInterest: %s\taccountsToTransactions: %s", this.getName(), this.getIncomingInterest(), this.getOutgoingInterest(), accountsToTransactions);
    }

    /**
     * checks if two objects have equal values
     * @param p_object object to be checked against
     * @return true if they have equal values or false if not
     */
    @Override
    public boolean equals(Object p_object) {
        //check if this is the same as comparison object
        if(this == p_object) {
            return true;
        }
        //check if current object has same class as comparison object, and if comparison object is initialized
        if(!(p_object instanceof PrivateBank)) {
            return false;
        }
        //cast and compare
        PrivateBank object = (PrivateBank) p_object;
        return this.getName().equals(object.getName()) &&
                this.getIncomingInterest() == object.getIncomingInterest() &&
                this.getOutgoingInterest() == object.getOutgoingInterest() &&
                this.accountsToTransactions.equals(object.accountsToTransactions);
    }

    /**
     * create a new account
     * @param p_account the account to be added
     * @throws AccountAlreadyExistsException account already exists
     */
    @Override
    public void createAccount(String p_account) throws AccountAlreadyExistsException {
        if (accountsToTransactions.containsKey(p_account)) {
            throw new AccountAlreadyExistsException();
        }
        //type inference of generics with <>
        accountsToTransactions.put(p_account, new ArrayList<>());
    }

    /**
     * create account with already existing transactions
     * @param p_account the account to be added
     * @param p_transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException account already exists
     * @throws TransactionAlreadyExistException transaction already exists
     * @throws TransactionAttributeException transaction attribute invalid
     */
    @Override
    public void createAccount(String p_account, List<Transaction> p_transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        if (accountsToTransactions.containsKey(p_account)) {
            throw new AccountAlreadyExistsException();
        }
        Set<Transaction> duplicateTransactions = new HashSet<>();
        for (Transaction transaction : p_transactions) {
            if (!duplicateTransactions.add(transaction)) {
                throw new TransactionAlreadyExistException();
            }
            if (!transaction.validateTransactionAttributes()) {
                throw new TransactionAttributeException();
            }
        }
        accountsToTransactions.put(p_account, new ArrayList<>(p_transactions));
    }

    /**
     * add transaction to an account
     * @param p_account the account to which the transaction is added
     * @param p_transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException transaction already exists for account
     * @throws AccountDoesNotExistException account does not exist
     * @throws TransactionAttributeException transaction attribute invalid
     */
    @Override
    public void addTransaction(String p_account, Transaction p_transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, TransactionAttributeValidationException {
        p_transaction.overwriteInterest(this.getIncomingInterest(), this.getOutgoingInterest());

        if (!accountsToTransactions.containsKey(p_account)) {
            throw new AccountDoesNotExistException();
        }

        if (containsTransaction(p_account, p_transaction)) {
            throw new TransactionAlreadyExistException();
        }

        if (!p_transaction.validateTransactionAttributes()) {
            throw new TransactionAttributeException();
        }

        accountsToTransactions.get(p_account).add(p_transaction);
    }

    /**
     * remove transaction from account
     * @param p_account the account from which the transaction is removed
     * @param p_transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException account does not exist
     * @throws TransactionDoesNotExistException transaction does not exist
     */
    @Override
    public void removeTransaction(String p_account, Transaction p_transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!accountsToTransactions.containsKey(p_account)) {
            throw new AccountDoesNotExistException();
        }

        List<Transaction> accountTransactions = accountsToTransactions.get(p_account);
        if (!accountTransactions.remove(p_transaction)) {
            throw new TransactionDoesNotExistException();
        }
    }

    /**
     * checks if account contains transaction
     * @param p_account the account from which the transaction is checked
     * @param p_transaction the transaction to search/look for
     * @return true if account contains transaction or false if not
     */
    @Override
    public boolean containsTransaction(String p_account, Transaction p_transaction) {
        List<Transaction> accountTransactions = accountsToTransactions.get(p_account);
        //check for equal values or equal references? one test case is commented out bc of this
        return accountTransactions != null && accountTransactions.contains(p_transaction);
    }

    /**
     * return total account balance
     * @param p_account the selected account
     * @return value of all transactions added up
     */
    @Override
    public double getAccountBalance(String p_account) {
        List<Transaction> accountTransactions = accountsToTransactions.get(p_account);
        double balance = 0.0;
        if (accountTransactions != null) {
            for (Transaction transaction : accountTransactions) {
                balance += transaction.calculate();
            }
        }
        return balance;
    }

    /**
     * getter for all transactions of an account
     * @param p_account the selected account
     * @return list of all transactions of an account
     */
    @Override
    public List<Transaction> getTransactions(String p_account) {
        return accountsToTransactions.getOrDefault(p_account, new ArrayList<>());
    }

    /**
     * sorts transactions in ascending or descending order
     * @param p_account the selected account
     * @param p_asc selects if the transaction list is sorted in ascending(true) or descending(false) order
     * @return a list of transactions sorted by their amount
     */
    @Override
    public List<Transaction> getTransactionsSorted(String p_account, boolean p_asc) {
        List<Transaction> accountTransactions = accountsToTransactions.get(p_account);
        if (accountTransactions != null) {
            if (p_asc) {
                accountTransactions.sort(Comparator.comparingDouble(Transaction::getAmount));
            } else {
                accountTransactions.sort(Comparator.comparingDouble(Transaction::getAmount).reversed());
            }
            return new ArrayList<>(accountTransactions);
        }
        return new ArrayList<>();
    }

    /**
     * getter for transactions by deposits or withdrawals
     * @param p_account  the selected account
     * @param p_positive selects if positive(true) or negative(false) transactions are listed
     * @return list of positive or negative transactions
     */
    @Override
    public List<Transaction> getTransactionsByType(String p_account, boolean p_positive) {
        List<Transaction> accountTransactions = accountsToTransactions.get(p_account);
        List<Transaction> filteredTransactions = new ArrayList<>();
        if (accountTransactions != null) {
            for (Transaction transaction : accountTransactions) {
                if ((transaction.calculate() > 0. && p_positive) || (transaction.calculate() < 0. && !p_positive)) {
                    filteredTransactions.add(transaction);
                }
            }
        }
        return filteredTransactions;
    }
}
