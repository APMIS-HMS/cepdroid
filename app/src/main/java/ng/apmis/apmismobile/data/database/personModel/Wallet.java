package ng.apmis.apmismobile.data.database.personModel;

import java.util.List;

import ng.apmis.apmismobile.data.database.cardModel.Card;

public class Wallet {

    private Integer balance;

    private Integer ledgerBalance;

    private List<Transaction> transactions = null;

    private String createdAt;

    private String updatedAt;

    private String _id;

    private List<Card> cards;

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(Integer ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "balance=" + balance +
                ", ledgerBalance=" + ledgerBalance +
                ", transactions=" + transactions +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", _id='" + _id + '\'' +
                '}';
    }
}
