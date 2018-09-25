package ng.apmis.apmismobile.data.database.facilityModel;

/**
 * Class indicating the type of appointment.
 */
public class AppointmentType {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Name of the appointment type, usually "New" or "Follow Up"
     */
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}