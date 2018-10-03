package ng.apmis.apmismobile.data.database.documentationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mofeejegi-apmis.<br/>
 * An object containing meta-data about a particular {@link Document}
 * record.
 */
public class DocumentType implements Serializable{

    private String _id;
    private String updatedAt;
    private String createdAt;


    //Unique Ids
    private String typeOfDocumentId;
    private String scopeLevelId;
    private String facilityId;
    private List<Object> moduleIds = null;

    /**
     * Indicates the {@link Document} title, in the form of a readable string
     * such as "Vitals" or "Allergies"
     */
    private String title;

    /**
     * Body meta-data for this {@link Document}
     */
    private String body;

    private Boolean isSide;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTypeOfDocumentId() {
        return typeOfDocumentId;
    }

    public void setTypeOfDocumentId(String typeOfDocumentId) {
        this.typeOfDocumentId = typeOfDocumentId;
    }

    public String getScopeLevelId() {
        return scopeLevelId;
    }

    public void setScopeLevelId(String scopeLevelId) {
        this.scopeLevelId = scopeLevelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
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

    public Boolean getIsSide() {
        return isSide;
    }

    public void setIsSide(Boolean isSide) {
        this.isSide = isSide;
    }

    public List<Object> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<Object> moduleIds) {
        this.moduleIds = moduleIds;
    }

}
