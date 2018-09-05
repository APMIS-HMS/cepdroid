package ng.apmis.apmismobile.data.database.patientModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.model.PersonEntry;

public class Patient {

    private String _id;
    private String updatedAt;
    private String createdAt;
    private String personId;
    private String facilityId;
    private List<Object> timeLines = null;
    private List<Object> clientsNo = null;
    private List<Object> tags = null;
    private List<Object> orders = null;
    private List<PaymentPlan> paymentPlan = null;
    private Boolean isActive;
    private String age;
    private PersonEntry personDetails;
    private Facility facilityObj;

    public Patient(String _id, String updatedAt, String createdAt, String personId, String facilityId, List<Object> timeLines, List<Object> clientsNo, List<Object> tags, List<Object> orders, List<PaymentPlan> paymentPlan, Boolean isActive, String age, PersonEntry personDetails, Facility facilityObj) {
        this._id = _id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.personId = personId;
        this.facilityId = facilityId;
        this.timeLines = timeLines;
        this.clientsNo = clientsNo;
        this.tags = tags;
        this.orders = orders;
        this.paymentPlan = paymentPlan;
        this.isActive = isActive;
        this.age = age;
        this.personDetails = personDetails;
        this.facilityObj = facilityObj;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public List<Object> getTimeLines() {
        return timeLines;
    }

    public void setTimeLines(List<Object> timeLines) {
        this.timeLines = timeLines;
    }

    public List<Object> getClientsNo() {
        return clientsNo;
    }

    public void setClientsNo(List<Object> clientsNo) {
        this.clientsNo = clientsNo;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public List<Object> getOrders() {
        return orders;
    }

    public void setOrders(List<Object> orders) {
        this.orders = orders;
    }

    public List<PaymentPlan> getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(List<PaymentPlan> paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public PersonEntry getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(PersonEntry personDetails) {
        this.personDetails = personDetails;
    }

    public Facility getFacilityObj() {
        return facilityObj;
    }

    public void setFacilityObj(Facility facilityObj) {
        this.facilityObj = facilityObj;
    }

}
