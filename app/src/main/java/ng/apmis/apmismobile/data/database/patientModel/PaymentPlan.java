package ng.apmis.apmismobile.data.database.patientModel;

public class PaymentPlan {

    private String planType;
    private String _id;
    private Boolean isDefault;
    private Boolean planDetails;

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = _id;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(Boolean planDetails) {
        this.planDetails = planDetails;
    }

}
