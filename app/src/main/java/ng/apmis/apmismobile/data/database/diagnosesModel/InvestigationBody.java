package ng.apmis.apmismobile.data.database.diagnosesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ng.apmis.apmismobile.data.database.model.PersonEntry;

public class InvestigationBody {

    private Boolean isUrgent = false;

    private Boolean isExternal = false;

    @SerializedName("investigation")
    @Expose
    private InvestigationData investigation;

    private Boolean sampleTaken = false;

    private String sampleTakenBy;

    private Boolean specimenReceived = false;

    private String specimenNumber = "";

    private Report report;

    private Boolean isUploaded = false;

    private Boolean isSaved = false;



    private String labRequestDate;

    private PersonEntry labRequestEmployee;


    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public Boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(Boolean isExternal) {
        this.isExternal = isExternal;
    }

    public InvestigationData getInvestigation() {
        return investigation;
    }

    public void setInvestigation(InvestigationData investigation) {
        this.investigation = investigation;
    }

    public Boolean getSampleTaken() {
        return sampleTaken;
    }

    public void setSampleTaken(Boolean sampleTaken) {
        this.sampleTaken = sampleTaken;
    }

    public String getSampleTakenBy() {
        return sampleTakenBy;
    }

    public void setSampleTakenBy(String sampleTakenBy) {
        this.sampleTakenBy = sampleTakenBy;
    }

    public Boolean getSpecimenReceived() {
        return specimenReceived;
    }

    public void setSpecimenReceived(Boolean specimenReceived) {
        this.specimenReceived = specimenReceived;
    }

    public String getSpecimenNumber() {
        return specimenNumber;
    }

    public void setSpecimenNumber(String specimenNumber) {
        this.specimenNumber = specimenNumber;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Boolean getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(Boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public Boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(Boolean isSaved) {
        this.isSaved = isSaved;
    }




    public void setLabRequestDate(String labRequestDate){
        this.labRequestDate = labRequestDate;
    }

    public String getLabRequestDate() {
        return labRequestDate;
    }


    public PersonEntry getLabRequestEmployee() {
        return labRequestEmployee;
    }

    public void setLabRequestEmployee(PersonEntry labRequestEmployee) {
        this.labRequestEmployee = labRequestEmployee;
    }


}
