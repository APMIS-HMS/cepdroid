package ng.apmis.apmismobile.data.database.facilityModel;

import java.util.List;

/**
 * Created by mofeejegi-apmis.<br/>
 * Units are subsets of Departments and house the {@link Clinic}s
 */
public class Unit {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Abbreviated name of the Unit
     */
    private String shortName;

    /**
     * Name of the Unit
     */
    private String name;

    /**
     * Short description about the Unit and what occurs in it
     */
    private String description;

    /**
     * List of Clinics housed in this Unit
     */
    private List<Clinic> clinics = null;
    private Boolean isActive;



    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
