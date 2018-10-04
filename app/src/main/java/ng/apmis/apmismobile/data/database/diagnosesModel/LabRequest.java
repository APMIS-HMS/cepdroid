package ng.apmis.apmismobile.data.database.diagnosesModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.utilities.AppUtils;

public class LabRequest implements Comparable<LabRequest>{

    private String _id;
    private String updatedAt;
    private String createdAt;

    private String facilityId;

    private String patientId;

    private String labNumber;

    private String clinicalInformation;

    private String diagnosis;

    private String createdBy;

    @SerializedName("investigations")
    @Expose
    private List<InvestigationBody> investigationBodies = null;

    private PersonEntry employeeDetails;

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
