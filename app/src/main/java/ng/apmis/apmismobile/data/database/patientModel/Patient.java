package ng.apmis.apmismobile.data.database.patientModel;

import java.util.List;

import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

/**
 * A {@link PersonEntry} becomes a Patient when enrolled in a {@link Facility}
 * The Patient object has extra fields alongside the underlying PersonEntry
 */
public class Patient {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Unique Person id
     */
    private String personId;

    /**
     * Unique facility Id of the Facility in which this Patient is enrolled in
     */
    private String facilityId;

    /**
     * PaymentPlan of the Patient
     */
    private List<PaymentPlan> paymentPlan = null;
    private Boolean isActive;
    private String age;

    /**
     * Underlying Person Object of the Patient
     */
    private PersonEntry personDetails;

    /**
     * Facility in which the Patient is enrolled in. <br/>
     * <b>NOTE: Facility Object here refers to only metadata and should
     * not be used for calling deep data</b><br/>
     * Only the following fields can be called from here
     * <ul>
     *     <li>{@link Facility#_id}</li>
     *     <li>{@link Facility#name}</li>
     *     <li>{@link Facility#email}</li>
     *     <li>{@link Facility#shortName}</li>
     *     <li>{@link Facility#primaryContactPhoneNo}</li>
     * </ul>
     */
    private Facility facilityObj;

    public Patient(String _id, String updatedAt, String createdAt, String personId, String facilityId, List<PaymentPlan> paymentPlan, Boolean isActive, String age, PersonEntry personDetails, Facility facilityObj) {
        this._id = _id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.personId = personId;
        this.facilityId = facilityId;
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
