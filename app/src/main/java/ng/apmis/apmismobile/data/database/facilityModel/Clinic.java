package ng.apmis.apmismobile.data.database.facilityModel;

/**
 * Created by mofeejegi-apmis.<br/>
 * <code>Clinic</code> object representing a sub-set of a
 * <code>Facility</code> usually handling a particular medical field
 */
public class Clinic {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * The name of the clinic
     */
    private String clinicName;

    /**
     * Capacity in terms of <code>Patient</code> count, of this clinic
     */
    private Integer clinicCapacity;



    // Getters and Setters


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

