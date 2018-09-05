package ng.apmis.apmismobile.data.database.facilityModel;

import java.util.List;

import ng.apmis.apmismobile.data.database.model.PersonEntry;

public class Employee {

    private String _id;
    private String updatedAt;
    private String createdAt;
    private String facilityId;
    private String departmentId;
    private String minorLocationId;
    private String officialEmailAddress;
    private String personId;
    private String professionId;
    private List<String> units = null;
    private Boolean isActive;
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

