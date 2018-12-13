package ng.apmis.apmismobile.data.database.fundAccount;

import java.util.List;

import ng.apmis.apmismobile.data.database.facilityModel.Service;

public class BillManager {

    private List<Service> services = null;

    private Boolean canRemove;

    private String createdAt;

    private String updatedAt;

    private String _id;

    private String name;

    private String facilityId;


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

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    @Override
    public String toString() {
        return "BillManager{" +
                "services=" + services +
                ", canRemove=" + canRemove +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", _id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
