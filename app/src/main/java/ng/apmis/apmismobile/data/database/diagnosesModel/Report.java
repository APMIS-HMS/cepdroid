package ng.apmis.apmismobile.data.database.diagnosesModel;

public class Report {

    private String employeeId;

    private String patientId;

    private String facilityId;

    private String action;

    private String investigationId;

    private String labRequestId;

    private String conclusion;

    private String outcome;

    private String recommendation;

    private String publishedById;



    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(String investigationId) {
        this.investigationId = investigationId;
    }

    public String getLabRequestId() {
        return labRequestId;
    }

    public void setLabRequestId(String labRequestId) {
        this.labRequestId = labRequestId;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getPublishedById() {
        return publishedById;
    }

    public void setPublishedById(String publishedById) {
        this.publishedById = publishedById;
    }

}