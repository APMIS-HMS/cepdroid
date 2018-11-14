package ng.apmis.apmismobile.data.database.personModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("transactionType")
    @Expose
    private String transactionType;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("transactionMedium")
    @Expose
    private String transactionMedium;
    @SerializedName("transactionStatus")
    @Expose
    private String transactionStatus;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("ledgerBalance")
    @Expose
    private Integer ledgerBalance;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("paidBy")
    @Expose
    private String paidBy;
    @SerializedName("destinationType")
    @Expose
    private String destinationType;
    @SerializedName("destinationId")
    @Expose
    private String destinationId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("refCode")
    @Expose
    private String refCode;
    @SerializedName("sourceType")
    @Expose
    private String sourceType;
    @SerializedName("sourceId")
    @Expose
    private String sourceId;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTransactionMedium() {
        return transactionMedium;
    }

    public void setTransactionMedium(String transactionMedium) {
        this.transactionMedium = transactionMedium;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

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

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", transactionMedium='" + transactionMedium + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", balance=" + balance +
                ", ledgerBalance=" + ledgerBalance +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", paidBy='" + paidBy + '\'' +
                ", destinationType='" + destinationType + '\'' +
                ", destinationId='" + destinationId + '\'' +
                ", description='" + description + '\'' +
                ", refCode='" + refCode + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}