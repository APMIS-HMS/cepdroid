package ng.apmis.apmismobile.data.database.facilityModel;

import java.util.List;

import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

/**
 * <code>Employee</code> class containing <code>PersonEntry</code>
 * details alongside other information pertaining to profession and
 * <code>Facility</code> in which the <code>Employee</code> is working
 */
public class Employee {

    private String _id;
    private String updatedAt;
    private String createdAt;

    //Unique Ids
    private String facilityId;
    private String departmentId;
    private String minorLocationId;

    /**
     * Employees work email address assigned
     */
    private String officialEmailAddress;

    /**
     * Person Id for the PersonEntry object of this Employee
     */
    private String personId;

    /**
     * Readable Profession Id string representing
     * the Employees role/work in the Clinic/Facility
     */
    private String professionId;

    private List<String> units = null;
    private Boolean isActive;

    /**
     * The Employee's {@link PersonEntry} details
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getMinorLocationId() {
        return minorLocationId;
    }

    public void setMinorLocationId(String minorLocationId) {
        this.minorLocationId = minorLocationId;
    }

    public String getOfficialEmailAddress() {
        return officialEmailAddress;
    }

    public void setOfficialEmailAddress(String officialEmailAddress) {
        this.officialEmailAddress = officialEmailAddress;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public PersonEntry getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(PersonEntry personDetails) {
        this.personDetails = personDetails;
    }

}

