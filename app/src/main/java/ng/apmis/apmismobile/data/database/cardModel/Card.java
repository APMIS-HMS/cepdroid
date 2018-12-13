package ng.apmis.apmismobile.data.database.cardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("authorization")
    @Expose
    private Authorization authorization;

    @SerializedName("customer")
    @Expose
    private Customer customer;



    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
