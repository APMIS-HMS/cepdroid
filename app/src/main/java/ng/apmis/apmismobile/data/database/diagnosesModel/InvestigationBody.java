package ng.apmis.apmismobile.data.database.diagnosesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

/**
 * Top level Investigation body item which comes as part of the {@link LabRequest}
 */
public class InvestigationBody {

    /**
     * Flag to check whether the investigation is of an urgent or normal priority
     */
    private Boolean isUrgent = false;

    /**
     * Check if investigation is carried out by an external facility
     */
    private Boolean isExternal = false;

    /**
     * Meta data about the investigation being carried out
     */
    @SerializedName("investigation")
    @Expose
    private InvestigationData investigation;

    /**
     * CHeck to see if the test sample has been taken from the Patient
     */
    private Boolean sampleTaken = false;

    /**
     * Name of the Employee who took the sample
     */
    private String sampleTakenBy;

    /**
     * Check to see if the specimen has been received and accepted by the lab of the facility
     */
    private Boolean specimenReceived = false;

    /**
     * Specimen number assigned to each specimen received
     */
    private String specimenNumber = "";

    /**
     * A Report object containing observations and results from this investigation
     */
    private Report report;

    /**
     * Check if a report for the investigation has been published
     */
    private Boolean isUploaded = false;

    /**
     * Check if a report for this investigation has been saved as a draft
     */
    private Boolean isSaved = false;



    //Laboratory Request Fields
    /**
     * Last Updated date of the LabRequest
     */
    private String labRequestDate;

    /**
     * Person details of the Employee who initiated the labRequest
     */
    private PersonEntry labRequestEmployee;

    /**
     * Statement issued by Employee concerning this request/investigation
     */
    private String labRequestClinicalInformation;

    /**
     * Lab number for identifying the laboratory for this request
     */
    private String labRequestNumber;

    /**
     *
     */
    private String labRequestDiagnosis;


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


    public String getLabRequestClinicalInformation() {
        return labRequestClinicalInformation;
    }

    public void setLabRequestClinicalInformation(String labRequestClinicalInformation) {
        this.labRequestClinicalInformation = labRequestClinicalInformation;
    }

    public String getLabRequestNumber() {
        return labRequestNumber;
    }

    public void setLabRequestNumber(String labRequestNumber) {
        this.labRequestNumber = labRequestNumber;
    }

    public String getLabRequestDiagnosis() {
        return labRequestDiagnosis;
    }

    public void setLabRequestDiagnosis(String labRequestDiagnosis) {
        this.labRequestDiagnosis = labRequestDiagnosis;
    }
}
