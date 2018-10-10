package ng.apmis.apmismobile.data.database.diagnosesModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * A request sent to the laboratory of a facility to investigate and diagnose
 * the issue with a Patient based on specimens received
 */
public class LabRequest implements Comparable<LabRequest> {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Id of the Facility in which this LaboratoryRequest was made
     */
    private String facilityId;

    /**
     * The id of the Patient being investigated
     */
    private String patientId;

    /**
     * Lab number for identifying the laboratory for this request
     */
    private String labNumber;

    /**
     * Statement issued by Employee concerning this request/investigation
     */
    private String clinicalInformation;

    /**
     * Initial diagnosis issued by Employee concerning this request/investigation
     */
    private String diagnosis;

    /**
     * Employee who initiated this laboratory request
     */
    private String createdBy;

    /**
     * List of all individual investigations initiated with this LabRequest
     */
    @SerializedName("investigations")
    @Expose
    private List<InvestigationBody> investigationBodies = null;

    /**
     * Person details of the Employee
     */
    private PersonEntry employeeDetails;

    /**
     * Person details of the Patient
     */
    private PersonEntry personDetails;



    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getLabNumber() {
        return labNumber;
    }

    public void setLabNumber(String labNumber) {
        this.labNumber = labNumber;
    }

    public String getClinicalInformation() {
        return clinicalInformation;
    }

    public void setClinicalInformation(String clinicalInformation) {
        this.clinicalInformation = clinicalInformation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<InvestigationBody> getInvestigationBodies() {
        return investigationBodies;
    }

    public void setInvestigationBodies(List<InvestigationBody> investigationBodies) {
        this.investigationBodies = investigationBodies;
    }

    public PersonEntry getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(PersonEntry employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    public PersonEntry getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(PersonEntry personDetails) {
        this.personDetails = personDetails;
    }

    @Override
    public int compareTo(@NonNull LabRequest o) {
        long date1 = AppUtils.dbStringToLocalDate(getUpdatedAt()).getTime();
        long date2 = AppUtils.dbStringToLocalDate(o.getUpdatedAt()).getTime();

        //descending order
        return Long.compare(date1, date2);
    }

}
