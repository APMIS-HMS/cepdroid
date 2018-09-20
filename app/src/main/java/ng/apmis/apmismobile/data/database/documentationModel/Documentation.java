package ng.apmis.apmismobile.data.database.documentationModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ng.apmis.apmismobile.utilities.AppUtils;

public class Documentation implements Comparable<Documentation>, Serializable{

    @SerializedName("_id")
    @Expose
    private String id;
    private String createdBy;
    private String facilityId;
    private String facilityName;
    private String patientId;
    private String patientName;
    private Document document;
    private String updatedAt;
    private String createdAt;
    private String documentationStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
