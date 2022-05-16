package fr.sg.simplebanckaccount.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Double amount;
    private LocalDateTime date;
    private String description;

    public Transaction() {
    }

    public Transaction(Double amount, LocalDateTime date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Transaction amount(Double amount) {
        setAmount(amount);
        return this;
    }

    public Transaction date(LocalDateTime date) {
        setDate(date);
        return this;
    }

    public Transaction description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Transaction)) {
            return false;
        }
        Transaction transaction = (Transaction) o;
        return Objects.equals(amount, transaction.amount) && Objects.equals(date, transaction.date)
                && Objects.equals(description, transaction.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, date, description);
    }

    @Override
    public String toString() {
        return "{" +
                " amount='" + getAmount() + "'" +
                ", date='" + getDate() + "'" +
                ", description='" + getDescription() + "'" +
                "}";
    }

}
