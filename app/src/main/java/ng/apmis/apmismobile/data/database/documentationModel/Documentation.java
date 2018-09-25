package ng.apmis.apmismobile.data.database.documentationModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Created by mofeejegi-apmis.<br/>
 * Documentation object containing medical records for
 * a {@link Patient} in a {@link Facility}.
 */
public class Documentation implements Comparable<Documentation>, Serializable{
    
    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Practitioner who created this Documentation object
     */
    private String createdBy;

    /**
     * Unique Facility Id representing the {@link Facility} in which this
     * documentation was created
     */
    private String facilityId;
    private String facilityName;

    /**
     * Unique Patient Id for the {@link Patient} to whom
     * this documentation pertains to.
     */
    private String patientId;
    private String patientName;

    /**
     * Document object for housing the Documentation details
     */
    private Document document;

    /**
     * Status of the current Documentation, usually "Draft"
     */
    private String documentationStatus;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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

    public String getDocumentationStatus() {
        return documentationStatus;
    }

    public void setDocumentationStatus(String documentationStatus) {
        this.documentationStatus = documentationStatus;
    }

    @Override
    public int compareTo(@NonNull Documentation o) {
        long date1 = AppUtils.dbStringToLocalDate(getUpdatedAt()).getTime();
        long date2 = AppUtils.dbStringToLocalDate(o.getUpdatedAt()).getTime();

        //descending order
        return Long.compare(date1, date2);
    }
}
