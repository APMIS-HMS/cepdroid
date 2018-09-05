package ng.apmis.apmismobile.data.database.facilityModel;

public class Clinic {

    private String _id;
    private String clinicName;
    private String updatedAt;
    private String createdAt;
    private Integer clinicCapacity;

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
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

    public Integer getClinicCapacity() {
        return clinicCapacity;
    }

    public void setClinicCapacity(Integer clinicCapacity) {
        this.clinicCapacity = clinicCapacity;
    }

}

