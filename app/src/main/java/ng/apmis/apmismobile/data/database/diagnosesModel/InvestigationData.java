package ng.apmis.apmismobile.data.database.diagnosesModel;

/**
 * Meta data pertaining to the {@link InvestigationBody} investigation being carried out
 * by a Laboratory. It usually contains ongoing information on the process of the
 * investigation.
 */
public class InvestigationData {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Id of the facility processing these reports
     */
    private String facilityId;

    /**
     * Name used to identify this entire Investigation
     */
    private String name;

    /**
     * Measurement unit of the sample taken
     */
    private String unit;

    /**
     * The name of the specimen received
     */
    private String specimen;

    /**
     * Unique Id used to Identify the service of this investigation
     */
    private String serviceId;

    /**
     * Id used to Identify the service of this investigation by the facility
     */
    private String facilityServiceId;

    /**
     * Check to see of this investigation contains a Panel of other sub investigations
     */
    private Boolean isPanel;




    public Boolean getIsPanel() {
        return isPanel;
    }

    public void setIsPanel(Boolean isPanel) {
        this.isPanel = isPanel;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getFacilityServiceId() {
        return facilityServiceId;
    }

    public void setFacilityServiceId(String facilityServiceId) {
        this.facilityServiceId = facilityServiceId;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

}

