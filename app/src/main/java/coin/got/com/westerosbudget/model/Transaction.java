
package coin.got.com.westerosbudget.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prateek on 11/8/15.
 */

/**
 * Transaction Model class
 */
public class Transaction {
    private String id;
    private float amount;
    private String description;

    public enum TransactionState {
        @SerializedName("Unverified")
        UNVERIFIED, @SerializedName("Verified")
        VERIFIED, @SerializedName("Fraud")
        FRAUD;
    }

    public enum TransactionCategory {
        @SerializedName("Recharge")
        RECHARGE, @SerializedName("Taxi")
        TAXI
    }

    private TransactionState state;
    private TransactionCategory category;

    @SerializedName("time")
    private String date;

    public Transaction(Transaction t) {
        this.id = t.id;
        this.amount = t.amount;
        this.description = t.description;
        this.state = t.state;
        this.category = t.category;
        this.date = t.date;
    }

    public float getAmount() {
        return amount;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public TransactionState getState() {
        return state;
    }

}
