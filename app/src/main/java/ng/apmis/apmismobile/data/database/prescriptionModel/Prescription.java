package ng.apmis.apmismobile.data.database.prescriptionModel;

import android.support.annotation.NonNull;

import java.util.List;

import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Prescription Body containing list of {@link PrescriptionItem}s
 */
public class Prescription implements Comparable<Prescription> {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Unique Facility Id
     */
    private String facilityId;

    /**
     * Unique Employee Id
     */
    private String employeeId;

    /**
     * Priority the Patient should place in obtaining and taking this prescriptions
     * Usually between "Normal" and "Urgent"
     */
    private Priority priority;

    /**
     * Unique Patient id
     */
    private String patientId;

    /**
     * Flag to check whether the {@link PrescriptionItem} cost is charged by the Facility
     */
    private Boolean isCosted;

    /**
     * Check to see if Prescription has been dispensed
     */
    private Boolean isDispensed;

    /**
     * Flag to determine whether or not Prescription has been authorised by the Employee(Prescriber)
     */
    private Boolean isAuthorised;

    /**
     * List of Individual Prescription items contained in this Prescription Body
     */
    private List<PrescriptionItem> prescriptionItems = null;

    /**
     * Details of the employee (Prescriber of the items)
     */
    private PersonEntry employeeDetails;

    /**
     * Person Details of the patient for whom the items are prescribed for
     */
    private PersonEntry personDetails;




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

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Boolean getIsCosted() {
        return isCosted;
    }

    public void setIsCosted(Boolean isCosted) {
        this.isCosted = isCosted;
    }

    public Boolean getIsDispensed() {
        return isDispensed;
    }

    public void setIsDispensed(Boolean isDispensed) {
        this.isDispensed = isDispensed;
    }

    public Boolean getIsAuthorised() {
        return isAuthorised;
    }

    public void setIsAuthorised(Boolean isAuthorised) {
        this.isAuthorised = isAuthorised;
    }

    public List<PrescriptionItem> getPrescriptionItems() {
        return prescriptionItems;
    }

    public void setPrescriptionItems(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
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
    public int compareTo(@NonNull Prescription o) {
        long date1 = AppUtils.dbStringToLocalDate(getUpdatedAt()).getTime();
        long date2 = AppUtils.dbStringToLocalDate(o.getUpdatedAt()).getTime();

        //descending order
        return Long.compare(date1, date2);
    }


    /**
     * Priority class representing the priority the {@link Patient}
     * should place in obtaining and taking prescriptions.
     */
    private static class Priority {

        /**
         * Name of the Priority
         * Usually between "Normal" and "Urgent"
         */
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
}
