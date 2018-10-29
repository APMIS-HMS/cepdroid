package ng.apmis.apmismobile.data.database.diagnosesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Report created containing the results and observations from an investigation
 */
public class Report {

    /**
     * Id of the employee (in the facility) responsible for the investigation
     */
    private String employeeId;

    /**
     * Id of the Patient (in the facility) being investigated
     */
    private String patientId;

    /**
     * Facility id of the Facility
     */
    private String facilityId;

    /**
     * Current action taken on this report
     */
    private String action;

    /**
     * ID used to identify the investigation
     */
    private String investigationId;

    /**
     * Id used to identify the labRequest
     */
    private String labRequestId;

    /**
     * Conclusion of the investigation
     */
    private String conclusion;

    /**
     * General outcome of the result: Normal or Abnormal
     */
    private String outcome;

    /**
     * Best practices, prescriptions and words of advice to follow, issued by the doctor
     */
    private String recommendation;


    private String publishedById;

    /**
     * Detailed list of results gotten from the tests
     */
    @SerializedName("result")
    @Expose
    private List<Result> results = null;



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

    public List<Result> getResults() {
        return results;
    }

    public void setResult(List<Result> results) {
        this.results = results;
    }



    public static class Result {
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}