package fr.sg.simplebanckaccount.domain;

import java.util.List;
import java.util.Objects;

public class Account {

    private String user;
    private String password;
    private Double balance;
    private List<Transaction> transaction;

    public Account() {
    }

    public Account(String user, String password, Double balance, List<Transaction> transaction) {
        this.user = user;
        this.password = password;
        this.balance = balance;
        this.transaction = transaction;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransaction() {
        return this.transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }

    public void addTransaction(Transaction transaction) {
        this.transaction.add(transaction);
    }

    public Account user(String user) {
        setUser(user);
        return this;
    }

    public Account password(String password) {
        setPassword(password);
        return this;
    }

    public Account balance(Double balance) {
        setBalance(balance);
        return this;
    }

    public Account transaction(List<Transaction> transaction) {
        setTransaction(transaction);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(user, account.user) && Objects.equals(password, account.password)
                && Objects.equals(balance, account.balance) && Objects.equals(transaction, account.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, password, balance, transaction);
    }

    @Override
    public String toString() {
        return "{" +
                " user='" + getUser() + "'" +
                ", password='" + getPassword() + "'" +
                ", balance='" + getBalance() + "'" +
                ", transaction='" + getTransaction() + "'" +
                "}";
    }

}
