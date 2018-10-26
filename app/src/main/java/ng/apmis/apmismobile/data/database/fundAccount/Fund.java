package ng.apmis.apmismobile.data.database.fundAccount;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class Fund {

    private String beneficiaryName;
    private String fundTitle;
    private String amount;
    private String date;

    public Fund(String beneficiaryName, String fundTitle, String amount, String date) {
        this.beneficiaryName = beneficiaryName;
        this.fundTitle = fundTitle;
        this.amount = amount;
        this.date = date;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getFundTitle() {
        return fundTitle;
    }

    public void setFundTitle(String fundTitle) {
        this.fundTitle = fundTitle;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Fund{" +
                "beneficiaryName='" + beneficiaryName + '\'' +
                ", fundTitle='" + fundTitle + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
