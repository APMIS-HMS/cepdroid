package ng.apmis.apmismobile.data.database.facilityModel;

import java.util.List;

/**
 * A Category represents a group of {@link Service}s
 * rendered/offered by a Facility
 */
public class Category {

    private String _id;
    private String createdAt;
    private String updatedAt;

    /**
     * List of all Services in the Category
     */
    private List<Service> services = null;
    private Boolean canRemove;

    /**
     * The name of the Category, as decided by the Clinic/Facility
     */
    private String name;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Boolean getCanRemove() {
        return canRemove;
    }

    public void setCanRemove(Boolean canRemove) {
        this.canRemove = canRemove;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

