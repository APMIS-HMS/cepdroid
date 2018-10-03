package ng.apmis.apmismobile.data.database.appointmentModel;

/**
 * Created by mofeejegi-apmis. <br/>
 * Class representing the status of an appointment scheduled
 * Status types are within a finite list of options, e.g Scheduled, Active, Completed, etc
 */
public class OrderStatus {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Status type names
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