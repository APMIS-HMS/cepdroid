package ng.apmis.apmismobile.data.database.cardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Card Authorization object used to save and perform recurring charges on a card
 */
public class Authorization {

    @SerializedName("authorization_code")
    @Expose
    private String authorizationCode;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("last4")
    @Expose
    private String last4;
    @SerializedName("exp_month")
    @Expose
    private String expMonth;
    @SerializedName("exp_year")
    @Expose
    private String expYear;
    @SerializedName("bin")
    @Expose
    private String bin;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("channel")
    @Expose
    private String channel;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("reusable")
    @Expose
    private Boolean reusable;
    @SerializedName("country_code")
    @Expose
    private String countryCode;

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Boolean getReusable() {
        return reusable;
    }

    public void setReusable(Boolean reusable) {
        this.reusable = reusable;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
