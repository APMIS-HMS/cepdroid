package ng.apmis.apmismobile.data.database.facilityModel;

/**
 * Created by mofeejegi.<br/>
 * Class representing medical services offered by a <code>Clinic</code>
 */
public class Service {

    private String _id;
    private String createdAt;
    private String updatedAt;

    /**
     * Name/title of the Service
     */
    private String name;

    /**
     * Code name (short representation) of the Service
     */
    private String code;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
