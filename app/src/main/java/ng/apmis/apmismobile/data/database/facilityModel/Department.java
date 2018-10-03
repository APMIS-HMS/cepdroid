package ng.apmis.apmismobile.data.database.facilityModel;

import java.util.List;

/**
 * A representation of the various available Departments in a {@link Facility}
 */
public class Department {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * The name of the Facility department
     */
    private String name;

    private Boolean isActive;

    /**
     * List of Units in the department
     */
    private List<Unit> units = null;




    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

}

